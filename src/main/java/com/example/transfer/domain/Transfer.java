package com.example.transfer.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transfers", indexes = {
        @Index(name = "idx_transfer_idempotency_key", columnList = "idempotencyKey", unique = true)
})
public class Transfer {
    @Id
    private UUID id;

    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    private String failureReason;
    private Instant createdAt;
    private Instant updatedAt;

    protected Transfer() {}

    public Transfer(UUID id, String fromAccount, String toAccount, BigDecimal amount, String idempotencyKey) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.idempotencyKey = idempotencyKey;
        this.status = TransferStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void complete() {
        this.status = TransferStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }

    public void fail(String reason) {
        this.status = TransferStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public BigDecimal getAmount() { return amount; }
    public TransferStatus getStatus() { return status; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getFailureReason() { return failureReason; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
