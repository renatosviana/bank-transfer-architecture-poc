import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 50,
  duration: '30s',
};

// First create one transfer, copy its transferId here, then run this script.
// This simulates many repeated GET calls and should benefit from cache.
const transferId = __ENV.TRANSFER_ID;

export default function () {
  const res = http.get(`http://localhost:8080/transfers/${transferId}`);

  check(res, {
    'get transfer ok': (r) => r.status === 200,
  });

  sleep(0.2);
}
