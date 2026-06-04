# Architecture

## Purpose

This implementation demonstrates architectural patterns commonly used in enterprise financial systems.

The focus is on:

- Reliability
- Scalability
- Resilience
- Asynchronous Processing
- Operational Recoverability

---

# High-Level Architecture

```text
                    +----------------+
                    |     Client     |
                    +-------+--------+
                            |
                            v

                    +----------------+
                    |     Nginx      |
                    +-------+--------+
                            |
             +--------------+--------------+
             |                             |
             v                             v

        +---------+                  +---------+
        |  App1   |                  |  App2   |
        +----+----+                  +----+----+
             |                            |
             +------------+---------------+
                          |
                          v

                  +---------------+
                  | PostgreSQL    |
                  +---------------+

                          |
                          v

                  +---------------+
                  | Redis Cache   |
                  +---------------+

                          |
                          v

                  +---------------+
                  | Kafka         |
                  +-------+-------+
                          |
                          v

                +-------------------+
                | Transfer Consumer |
                +---------+---------+
                          |
                          v

                +-------------------+
                | Payment Service   |
                +---------+---------+
                          |
             +------------+-------------+
             |                          |
             v                          v

          Retry                 Circuit Breaker

                          |
                          v

                +-------------------+
                | Dead Letter Queue |
                +-------------------+
```

---

# Architectural Topics Demonstrated

## Idempotency

Goal:

Prevent duplicate transfer creation during retries or concurrent requests.

Implementation:

- HTTP Idempotency-Key
- Persistence-level uniqueness
- Application-level verification

Result:

Repeated requests return the same transfer.

---

## Retry Strategy

Goal:

Recover from transient failures.

Implementation:

- Resilience4j Retry
- Configurable backoff

Result:

Temporary failures automatically recover.

---

## Circuit Breaker

Goal:

Protect downstream services.

Implementation:

- Failure thresholds
- Open / Half-Open / Closed states
- Fast-fail behaviour

Result:

Prevents cascading failures.

---

## Dead Letter Queue

Goal:

Preserve failed transactions.

Implementation:

- Failed event persistence
- Replay support
- Full failure context retention

Result:

Zero failed transfer loss.

---

## Redis Caching

Goal:

Reduce database load.

Implementation:

- Cacheable transfer retrieval
- UUID cache keys
- TTL-based expiration

Result:

Reduced repeated database access.

---

## Kafka Event Streaming

Goal:

Decouple request handling from transfer processing.

Implementation:

- Producer
- Consumer
- DLQ topic
- KRaft deployment

Result:

Asynchronous transfer processing.

---

## Horizontal Scaling

Goal:

Scale the application tier independently.

Implementation:

- app1
- app2
- Nginx load balancing

Result:

Multiple stateless application instances process requests.

Important:

This proof of concept demonstrates horizontal scaling of the application tier.

Infrastructure components remain single-node.

Production deployments would typically introduce:

- PostgreSQL replication
- Redis clustering
- Multi-broker Kafka clusters

---

## Batch Processing

Goal:

Support large-volume processing.

Implementation:

- Spring Batch
- ItemReader
- ItemProcessor
- ItemWriter
- Chunk-oriented processing

Result:

Restartable and checkpointed execution model.

---

# Architecture Decisions

| Decision | Reason |
|-----------|---------|
| Kafka | Event-driven communication |
| PostgreSQL | Transactional consistency |
| Redis | Low-latency retrieval |
| Retry | Recovery from transient failures |
| Circuit Breaker | Failure isolation |
| DLQ | Operational recovery |
| Nginx | Load balancing |
| Docker Compose | Reproducible deployment |

---

# Operational Validation

Validated:

- PostgreSQL
- Redis
- Kafka KRaft
- Idempotency
- Retry
- Circuit Breaker
- DLQ
- Horizontal Scaling
- Load Balancing
- Concurrent Request Processing

---

# Conclusion

This proof of concept validates a set of architecture patterns frequently used in enterprise banking platforms and demonstrates their implementation through a working end-to-end system.