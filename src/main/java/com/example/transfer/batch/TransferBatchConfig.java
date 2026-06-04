package com.example.transfer.batch;

import com.example.transfer.domain.Transfer;
import com.example.transfer.repository.TransferRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

// Simple in-memory batch example.
// Architect explanation: read -> process -> write, commit per chunk, restart/retry can be added later.
@Configuration
public class TransferBatchConfig {

    @Bean
    Job transferImportJob(JobRepository jobRepository, Step transferImportStep) {
        return new JobBuilder("transferImportJob", jobRepository)
                .start(transferImportStep)
                .build();
    }

    @Bean
    Step transferImportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<Transfer> reader,
            ItemProcessor<Transfer, Transfer> processor,
            ItemWriter<Transfer> writer
    ) {
        return new StepBuilder("transferImportStep", jobRepository)
                .<Transfer, Transfer>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    ItemReader<Transfer> reader() {
        List<Transfer> sampleTransfers = List.of(
                new Transfer(UUID.randomUUID(), "A100", "B200", new BigDecimal("10.00"), "batch-1"),
                new Transfer(UUID.randomUUID(), "A101", "B201", new BigDecimal("20.00"), "batch-2")
        );

        AtomicInteger index = new AtomicInteger(0);

        return () -> {
            int current = index.getAndIncrement();
            return current < sampleTransfers.size() ? sampleTransfers.get(current) : null;
        };
    }

    @Bean
    ItemProcessor<Transfer, Transfer> processor() {
        return transfer -> transfer;
    }

    @Bean
    ItemWriter<Transfer> writer(TransferRepository transferRepository) {
        return chunk -> transferRepository.saveAll(chunk.getItems());
    }
}
