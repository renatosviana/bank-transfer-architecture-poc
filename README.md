# Bank Transfer Architecture POC

A reference implementation demonstrating architecture patterns commonly used in enterprise banking and financial systems.

## Purpose

This proof of concept was created to validate architectural approaches discussed during an architecture evaluation, including:

- Idempotency
- Event-driven processing
- Retry strategies
- Circuit breakers
- Dead letter queues
- Distributed caching
- Horizontal scaling
- Load balancing
- Batch processing

---

## Technology Stack

| Component | Technology |
|------------|------------|
| API | Spring Boot 3 + OpenAPI |
| Database | PostgreSQL 16 |
| Cache | Redis 7 |
| Messaging | Apache Kafka 7.7 (KRaft) |
| Resilience | Resilience4j |
| Batch | Spring Batch |
| Load Balancer | Nginx |
| Build | Gradle |
| Containerization | Docker Compose |

---

## Architecture Capabilities Demonstrated

- REST APIs
- API-First Design
- PostgreSQL Persistence
- Redis Caching
- Kafka Event Streaming
- Asynchronous Processing
- Idempotency
- Retry Strategies
- Circuit Breakers
- Dead Letter Queues
- Horizontal Scaling
- Load Balancing
- Batch Processing
- Containerized Deployment

---

## Running the Platform

### Docker Compose

```bash
docker compose up --build
```

Verify:

```bash
docker compose ps
```

Expected services:

- postgres
- redis
- kafka
- app1
- app2
- nginx

---

## API Explorer

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Sample Request

```bash
curl -X POST http://localhost:8080/transfers \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: transfer-001" \
  -d '{
    "fromAccount":"ACC001",
    "toAccount":"ACC002",
    "amount":100.00
  }'
```

---

## Validation Summary

### Idempotency

Validated repeated requests using identical idempotency keys.

Result:

- Same transfer ID returned
- No duplicate processing

### Retry & Circuit Breaker

Validated:

- Retry attempts
- Backoff strategy
- Circuit breaker state transitions
- Fallback execution

### Dead Letter Queue

Validated:

- Failed transfer preservation
- Replay capability
- Full failure context retention

### Horizontal Scaling

Validated:

- Multiple stateless application instances
- Nginx traffic distribution
- Shared persistence layer

### Kafka

Validated:

- Producer
- Consumer
- DLQ topic
- KRaft deployment mode

### Load Testing

Executed using Grafana k6.

Results:

- 208 requests
- 100% successful checks
- 0 HTTP failures
- Average latency: 156 ms
- p95 latency: 940 ms

---

## Notes

This implementation focuses on architecture validation and demonstration of production-inspired patterns.

Infrastructure services (PostgreSQL, Redis, Kafka) are deployed as single-node components in this proof of concept. Production deployments would typically introduce replication, clustering, and multi-zone availability.