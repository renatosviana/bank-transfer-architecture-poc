import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  vus: 20,
  duration: '30s',
};

export default function () {
  const key = `load-${__VU}-${__ITER}`;

  const res = http.post(
    `${BASE_URL}/transfers`,
    JSON.stringify({
      fromAccount: 'A100',
      toAccount: 'B200',
      amount: 10.00,
    }),
    {
      headers: {
        'Content-Type': 'application/json',
        'Idempotency-Key': key,
      },
    }
  );

  check(res, {
    'create transfer accepted': (r) => r.status === 202,
  });

  sleep(1);
}