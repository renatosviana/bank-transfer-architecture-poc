package com.example.transfer.repository;

import com.example.transfer.domain.FailedTransferEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedTransferEventRepository extends JpaRepository<FailedTransferEvent, Long> {
}
