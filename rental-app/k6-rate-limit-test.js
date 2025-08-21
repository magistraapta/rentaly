import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

// Custom metrics
const rateLimitExceeded = new Rate('rate_limit_exceeded');
const successfulRequests = new Rate('successful_requests');

// Test configuration
export const options = {
  stages: [
    // Ramp up to 50 users over 30 seconds
    { duration: '30s', target: 50 },
    // Stay at 50 users for 2 minutes
    { duration: '2m', target: 50 },
    // Ramp down to 0 users over 30 seconds
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<2000'], // 95% of requests should be below 2s
    http_req_failed: ['rate<0.1'],     // Error rate should be less than 10%
    rate_limit_exceeded: ['rate<0.3'],  // Rate limit exceeded should be less than 30%
  },
};

// Test data
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const TEST_USER_ID = __ENV.TEST_USER_ID || 'test-user-123';

// Test scenarios
export default function () {
  const params = {
    headers: {
      'Content-Type': 'application/json',
      'User-Agent': 'k6-load-test',
    },
  };

  // Test 1: Global Rate Limiting - Multiple endpoints
  const endpoints = [
    '/v1/cars',
    '/v1/cars/1',
    '/actuator/health',
    '/v1/redis/health',
  ];

  endpoints.forEach((endpoint) => {
    const response = http.get(`${BASE_URL}${endpoint}`, params);
    
    if (response.status === 429) {
      // Rate limit exceeded
      rateLimitExceeded.add(1);
      console.log(`Rate limit exceeded for ${endpoint}: ${response.headers['X-Rate-Limit-Retry-After-Seconds']}s`);
      
      // Check rate limit headers
      check(response, {
        'has rate limit headers': (r) => r.headers['X-Rate-Limit-Retry-After-Seconds'] !== undefined,
        'has retry after header': (r) => r.headers['X-Rate-Limit-Retry-After-Seconds'] !== undefined,
      });
    } else if (response.status === 200) {
      successfulRequests.add(1);
      
      // Check rate limit info headers
      check(response, {
        'has remaining tokens header': (r) => r.headers['X-Rate-Limit-Remaining'] !== undefined,
        'has reset time header': (r) => r.headers['X-Rate-Limit-Reset'] !== undefined,
        'response time < 1000ms': (r) => r.timings.duration < 1000,
      });
    }
  });

  // Test 2: Auth Rate Limiting (simulate login attempts)
  const loginPayload = JSON.stringify({
    username: `user-${__VU}`,
    password: 'test-password',
  });

  const loginResponse = http.post(`${BASE_URL}/v1/auth/login`, loginPayload, {
    ...params,
    headers: {
      ...params.headers,
      'Content-Type': 'application/json',
    },
  });

  if (loginResponse.status === 429) {
    rateLimitExceeded.add(1);
    console.log(`Auth rate limit exceeded: ${loginResponse.headers['X-Rate-Limit-Retry-After-Seconds']}s`);
  } else if (loginResponse.status === 200 || loginResponse.status === 401) {
    successfulRequests.add(1);
  }

  // Test 3: Redis Health Check (should not be rate limited)
  const redisHealthResponse = http.get(`${BASE_URL}/v1/redis/health`, params);
  
  check(redisHealthResponse, {
    'redis health check successful': (r) => r.status === 200,
    'redis health response time < 500ms': (r) => r.timings.duration < 500,
  });

  // Test 4: Simulate burst requests to trigger rate limiting
  if (__VU % 10 === 0) { // Every 10th virtual user
    const burstRequests = 20; // Send 20 rapid requests
    
    for (let i = 0; i < burstRequests; i++) {
      const response = http.get(`${BASE_URL}/v1/cars`, params);
      
      if (response.status === 429) {
        rateLimitExceeded.add(1);
        break; // Stop burst if rate limited
      }
      
      sleep(0.1); // Small delay between burst requests
    }
  }

  // Random sleep between requests to simulate real user behavior
  sleep(Math.random() * 2 + 1); // 1-3 seconds
}

// Setup function (runs once before the test)
export function setup() {
  console.log('Starting k6 rate limit test...');
  console.log(`Base URL: ${BASE_URL}`);
  console.log(`Test User ID: ${TEST_USER_ID}`);
  
  // Test basic connectivity
  const healthResponse = http.get(`${BASE_URL}/actuator/health`);
  
  if (healthResponse.status !== 200) {
    throw new Error(`Application not accessible at ${BASE_URL}`);
  }
  
  console.log('Application is accessible, starting load test...');
}

// Teardown function (runs once after the test)
export function teardown(data) {
  console.log('Load test completed');
  console.log('Final metrics:');
  console.log(`- Rate limit exceeded: ${rateLimitExceeded.values.rate}`);
  console.log(`- Successful requests: ${successfulRequests.values.rate}`);
}

// Handle different response scenarios
export function handleSummary(data) {
  return {
    'k6-rate-limit-results.json': JSON.stringify(data, null, 2),
    stdout: textSummary(data, { indent: ' ', enableColors: true }),
  };
}
