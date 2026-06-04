package com.example.transfer.service;

import com.example.transfer.domain.Transfer;
import com.example.transfer.dto.CreateTransferRequest;
import com.example.transfer.dto.TransferResponse;
import com.example.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.example.transfer.service.kafka.TransferEventPublisher;
import java.util.Optional;

import java.util.UUID;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final TransferProcessor transferProcessor;
    private final Optional<TransferEventPublisher> transferEventPublisher;

    public TransferService(
            TransferRepository transferRepository,
            TransferProcessor transferProcessor,
            Optional<TransferEventPublisher> transferEventPublisher
    ) {
        this.transferRepository = transferRepository;
        this.transferProcessor = transferProcessor;
        this.transferEventPublisher = transferEventPublisher;
    }

    @Transactional
    public TransferResponse createTransfer(String idempotencyKey, CreateTransferRequest request) {
        return transferRepository.findByIdempotencyKey(idempotencyKey)
                .map(TransferResponse::from)
                .orElseGet(() -> {
                    Transfer transfer = new Transfer(
                            UUID.randomUUID(),
                            request.fromAccount(),
                            request.toAccount(),
                            request.amount(),
                            idempotencyKey
                    );

                    Transfer saved = transferRepository.save(transfer);

                    // Async processing: API returns PENDING, processor completes later.
                    // If Kafka is enabled, publish an event. Otherwise, process with @Async.
                    transferEventPublisher.ifPresentOrElse(
                            publisher -> publisher.publish(saved.getId()),
                            () -> transferProcessor.process(saved.getId())
                    );

                    return TransferResponse.from(saved);
                });
    }

    @Cacheable(value = "transferStatus", key = "#id")
    public TransferResponse getTransfer(UUID id) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found: " + id));
        return TransferResponse.from(transfer);
    }
}
