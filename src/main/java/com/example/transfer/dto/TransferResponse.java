package com.example.transfer.dto;

import com.example.transfer.domain.Transfer;
import com.example.transfer.domain.TransferStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferResponse(
        UUID transferId,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        TransferStatus status,
        String failureReason
) {
    public static TransferResponse from(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getFromAccount(),
                transfer.getToAccount(),
                transfer.getAmount(),
                transfer.getStatus(),
                transfer.getFailureReason()
        );
    }
}
