import http from 'k6/http';
import { check, sleep } from 'k6';

// Burst test configuration
export const options = {
  stages: [
    // Normal load for 30 seconds
    { duration: '30s', target: 5 },
    // Burst to 50 users for 1 minute
    { duration: '1m', target: 50 },
    // Back to normal for 30 seconds
    { duration: '30s', target: 5 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<3000'],
    http_req_failed: ['rate<0.2'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  // Test multiple endpoints to trigger rate limiting
  const endpoints = [
    '/v1/cars',
    '/v1/cars/1',
    '/actuator/health',
    '/v1/redis/health',
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

  sleep(1); // Normal delay between iterations
}
