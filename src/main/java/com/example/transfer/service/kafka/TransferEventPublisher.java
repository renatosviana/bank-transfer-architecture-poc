package com.example.transfer.service.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class TransferEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String transferTopic;

    public TransferEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${app.kafka.transfer-topic}") String transferTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.transferTopic = transferTopic;
    }

    public void publish(UUID transferId) {
        kafkaTemplate.send(transferTopic, transferId.toString(), transferId.toString());
    }
}
