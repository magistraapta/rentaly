import http from 'k6/http';
import { check, sleep } from 'k6';

// Simple test configuration
export const options = {
  vus: 10,        // 10 virtual users
  duration: '1m', // Run for 1 minute
  thresholds: {
    http_req_duration: ['p(95)<2000'],
    http_req_failed: ['rate<0.1'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  // Test rate limiting on cars endpoint
  const response = http.get(`${BASE_URL}/v1/cars`);
  
  // Check response
  check(response, {
    'status is 200 or 429': (r) => r.status === 200 || r.status === 429,
    'has rate limit headers when 429': (r) => {
      if (r.status === 429) {
        return r.headers['X-Rate-Limit-Retry-After-Seconds'] !== undefined;
      }
      return true;
    },
    'has rate limit info when 200': (r) => {
      if (r.status === 200) {
        return r.headers['X-Rate-Limit-Remaining'] !== undefined;
      }
      return true;
    },
  });

  // Log rate limit info
  if (response.status === 429) {
    console.log(`Rate limited! Retry after: ${response.headers['X-Rate-Limit-Retry-After-Seconds']}s`);
  } else if (response.status === 200) {
    console.log(`Success! Remaining: ${response.headers['X-Rate-Limit-Remaining']}`);
  }

  sleep(1); // Wait 1 second between requests
}
