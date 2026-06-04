package com.example.transfer.service;

import com.example.transfer.domain.FailedTransferEvent;
import com.example.transfer.domain.Transfer;
import com.example.transfer.repository.FailedTransferEventRepository;
import com.example.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransferProcessor {
    private final TransferRepository transferRepository;
    private final FailedTransferEventRepository failedTransferEventRepository;
    private final DownstreamPaymentClient downstreamPaymentClient;

    public TransferProcessor(
            TransferRepository transferRepository,
            FailedTransferEventRepository failedTransferEventRepository,
            DownstreamPaymentClient downstreamPaymentClient
    ) {
        this.transferRepository = transferRepository;
        this.failedTransferEventRepository = failedTransferEventRepository;
        this.downstreamPaymentClient = downstreamPaymentClient;
    }

    @Async
    @Transactional
    @CacheEvict(value = "transferStatus", key = "#transferId")
    public void process(UUID transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found: " + transferId));

        try {
            downstreamPaymentClient.sendPayment(transfer);
            transfer.complete();
        } catch (Exception ex) {
            transfer.fail(ex.getMessage());

            // Simple DLQ simulation. Later replace with Kafka DLQ topic.
            failedTransferEventRepository.save(
                    new FailedTransferEvent(transfer.getId(), ex.getMessage())
            );
        }

        transferRepository.save(transfer);
    }
}
