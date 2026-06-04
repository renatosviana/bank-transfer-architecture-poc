#!/usr/bin/env bash
set -e

echo "Create one transfer"
CREATE_RESPONSE=$(curl -s -X POST http://localhost:8080/transfers \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: demo-123" \
  -d '{"fromAccount":"A100","toAccount":"B200","amount":250.00}')

echo "$CREATE_RESPONSE"

TRANSFER_ID=$(echo "$CREATE_RESPONSE" | sed -n 's/.*"transferId":"\([^"]*\)".*/\1/p')

echo "GET transfer many times to test cache path"
for i in {1..10}; do
  curl -s http://localhost:8080/transfers/$TRANSFER_ID
  echo
done

echo "DLQ table"
curl -s http://localhost:8080/admin/dlq
echo
