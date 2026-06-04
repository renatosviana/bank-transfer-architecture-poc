package com.example.transfer.api;

import com.example.transfer.domain.FailedTransferEvent;
import com.example.transfer.repository.FailedTransferEventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final FailedTransferEventRepository failedTransferEventRepository;

    public AdminController(FailedTransferEventRepository failedTransferEventRepository) {
        this.failedTransferEventRepository = failedTransferEventRepository;
    }

    @GetMapping("/dlq")
    public List<FailedTransferEvent> dlq() {
        return failedTransferEventRepository.findAll();
    }
}
