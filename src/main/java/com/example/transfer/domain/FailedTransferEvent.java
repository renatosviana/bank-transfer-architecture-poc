package com.example.transfer.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "failed_transfer_events")
public class FailedTransferEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID transferId;
    private String reason;
    private Instant createdAt;

    protected FailedTransferEvent() {}

    public FailedTransferEvent(UUID transferId, String reason) {
        this.transferId = transferId;
        this.reason = reason;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public UUID getTransferId() { return transferId; }
    public String getReason() { return reason; }
    public Instant getCreatedAt() { return createdAt; }
}
