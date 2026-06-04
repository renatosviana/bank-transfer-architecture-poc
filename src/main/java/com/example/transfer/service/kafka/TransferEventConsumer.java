package com.example.transfer.service.kafka;

import com.example.transfer.service.TransferProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class TransferEventConsumer {
    private final TransferProcessor transferProcessor;

    public TransferEventConsumer(TransferProcessor transferProcessor) {
        this.transferProcessor = transferProcessor;
    }

    @KafkaListener(topics = "${app.kafka.transfer-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String transferId) {
        transferProcessor.process(UUID.fromString(transferId));
    }
}
