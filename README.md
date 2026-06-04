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

## Validation Summary

### Idempotency

Validated repeated requests using identical idempotency keys.

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

```bash
curl http://localhost:8080/admin/dlq
```

Validated:

- Failed transfer preservation
- Replay capability
- Full failure context retention

### Horizontal Scaling & Load Balancing

Purpose: Verify that multiple stateless application instances are running behind Nginx and sharing external state through PostgreSQL and Redis.

```bash
docker compose ps
```

Validated:

- app1 and app2 running as separate stateless instances
- Nginx routing traffic through port 8080
- Shared PostgreSQL and Redis persistence/cache layers
- Application tier scaled horizontally

### Kafka

```bash
docker logs kafka --tail 50
```

Validated:

- Producer
- Consumer
- DLQ topic
- KRaft deployment mode

### Load Testing

Executed using Grafana k6.

Powershell command:
```bash
bank-transfer-architecture-lab-gradle-enhanced> docker run --rm -i `
>>   -v "${PWD}\load-tests:/scripts" `
>>   -e BASE_URL=http://host.docker.internal:8080 `
>>   grafana/k6 run /scripts/create-transfer.js
```

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