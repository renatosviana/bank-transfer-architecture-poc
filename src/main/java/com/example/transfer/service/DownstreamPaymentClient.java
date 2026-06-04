package com.example.transfer.service;

import com.example.transfer.domain.Transfer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DownstreamPaymentClient {
    private final Random random = new Random();

    @Value("${app.external-payment.simulated-failure-rate:0.30}")
    private double failureRate;

    @Retry(name = "paymentClient")
    @CircuitBreaker(name = "paymentClient", fallbackMethod = "fallback")
    public void sendPayment(Transfer transfer) {
        if (random.nextDouble() < failureRate) {
            throw new IllegalStateException("Simulated downstream payment failure");
        }

        // In real life: REST call, Kafka publish, or bank payment gateway call.
        System.out.println("Payment accepted by downstream for transfer " + transfer.getId());
    }

    public void fallback(Transfer transfer, Throwable throwable) {
        throw new IllegalStateException("Payment client unavailable after retries: " + throwable.getMessage());
    }
}
