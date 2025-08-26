import http from 'k6/http';
import { check, sleep } from 'k6';

// Burst test configuration
export const options = {
  stages: [
    { duration: '1m', target: 10 },
    { duration: '1m', target: 20 },
    { duration: '1m', target: 30 },
  ],

  thresholds: {
    http_req_failed: ['rate<0.001'], // the error rate must be lower than 0.1%
    http_req_duration: ['p(90)<2000'], // 90% of requests must complete below 2000ms
    http_req_receiving: ['max<17000'], // max receive request below 17000ms
    http_req_duration: ['p(99)<500'], // 99% of requests must complete below 5000ms
   },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  // Test multiple endpoints to trigger rate limiting
  const endpoints = [
    '/v1/cars',
  ];

  endpoints.forEach((endpoint) => {
    const response = http.get(`${BASE_URL}${endpoint}`);
    
    check(response, {
      [`${endpoint} - status is valid`]: (r) => r.status === 200 || r.status === 429,
      [`${endpoint} - has rate limit info`]: (r) => {
        if (r.status === 429) {
          return r.headers['X-Rate-Limit-Retry-After-Seconds'] !== undefined;
        }
        return true;
      },
    });

    // Log rate limit status
    if (response.status === 429) {
      console.log(`BURST: Rate limited on ${endpoint}, retry after: ${response.headers['X-Rate-Limit-Retry-After-Seconds']}s`);
    }
  });

  // Simulate rapid successive requests
  if (__VU > 20) { // Only high VU numbers do burst requests
    for (let i = 0; i < 5; i++) {
      const response = http.get(`${BASE_URL}/v1/cars`);
      
      if (response.status === 429) {
        console.log(`BURST: Rapid requests triggered rate limit, retry after: ${response.headers['X-Rate-Limit-Retry-After-Seconds']}s`);
        break;
      }
      
      sleep(0.1); // Very small delay between burst requests
    }
  }

  // Test get car by id
  for (let i = 1; i <= 10; i++) {
    const response = http.get(`${BASE_URL}/v1/cars/${i}`);

    check(response, {
      [`get car by id: ${i}`]: (r) => r.status === 200 || r.status === 429,
    });
  }

  sleep(1); // Normal delay between iterations
}