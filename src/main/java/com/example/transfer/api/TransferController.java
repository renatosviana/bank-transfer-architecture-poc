package com.example.transfer.api;

import com.example.transfer.dto.CreateTransferRequest;
import com.example.transfer.dto.TransferResponse;
import com.example.transfer.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TransferResponse createTransfer(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreateTransferRequest request
    ) {
        return transferService.createTransfer(idempotencyKey, request);
    }

    @GetMapping("/{id}")
    public TransferResponse getTransfer(@PathVariable UUID id) {
        return transferService.getTransfer(id);
    }
}
