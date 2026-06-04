import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 50,
  iterations: 50,
};

// All users send the SAME Idempotency-Key.
// Expected result: only one transfer row should be created because DB unique constraint protects idempotency.
export default function () {
  const res = http.post(
    'http://localhost:8080/transfers',
    JSON.stringify({
      fromAccount: 'A100',
      toAccount: 'B200',
      amount: 99.00,
    }),
    {
      headers: {
        'Content-Type': 'application/json',
        'Idempotency-Key': 'same-key-race-test',
      },
    }
  );

  check(res, {
    'accepted or existing returned': (r) => r.status === 202,
  });
}
