import type { CarListResponse, CarResponse, CarType } from '../types/api';

const API_BASE_URL = 'http://localhost:8080/v1';

export interface User {
  username: string;
  email: string;
  role: string;
}

export interface AuthResponse {
  success: boolean;
  message: string;
  data: {
    accessToken: string;
    refreshToken: string;
    user: User;
  };
}

export interface UserResponse {
  success: boolean;
  message: string;
  data: User;
}

class ApiService {
  private async request<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
      ...options,
    };

    // Add auth token if available
    const token = localStorage.getItem('token');
    if (token) {
      config.headers = {
        ...config.headers,
        'Authorization': `Bearer ${token}`,
      };
    }

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  private async requestAdmin<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
      ...options,
    };

    // Add admin auth token if available
    const adminToken = localStorage.getItem('adminToken');
    if (adminToken) {
      config.headers = {
        ...config.headers,
        'Authorization': `Bearer ${adminToken}`,
      };
    }

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      return await response.json();
    } catch (error) {
      console.error('Admin API request failed:', error);
      throw error;
    }
  }

  // Car API methods
  async getAllCars(): Promise<CarListResponse> {
    return this.request<CarListResponse>('/cars');
  }

  async getCarById(id: number): Promise<CarResponse> {
    return this.request<CarResponse>(`/cars/${id}`);
  }

  async getCarByName(name: string): Promise<CarResponse> {
    return this.request<CarResponse>(`/cars/name/${name}`);
  }

  async getCarsByType(type: CarType): Promise<CarListResponse> {
    return this.request<CarListResponse>(`/cars/type/${type}`);
  }

  // Authentication API methods
  async getCurrentUser(): Promise<UserResponse> {
    return this.request<UserResponse>('/auth/me');
  }

  async logout(): Promise<{ success: boolean; message: string }> {
    const response = await this.request<{ success: boolean; message: string }>('/auth/logout', {
      method: 'POST',
    });
    
    // Clear local storage on successful logout
    localStorage.removeItem('token');
    return response;
  }

  // Admin Authentication methods
  async getCurrentAdminUser(): Promise<UserResponse> {
    return this.requestAdmin<UserResponse>('/auth/me');
  }

  async adminLogout(): Promise<{ success: boolean; message: string }> {
    const response = await this.requestAdmin<{ success: boolean; message: string }>('/auth/logout', {
      method: 'POST',
    });
    
    // Clear admin storage on successful logout
    localStorage.removeItem('adminToken');
    localStorage.removeItem('adminUser');
    return response;
  }

  // Helper method to check if user is authenticated
  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return false;
    
    try {
      // Simple token expiration check (JWT tokens contain exp claim)
      const payload = JSON.parse(atob(token.split('.')[1]));
      const currentTime = Date.now() / 1000;
      return payload.exp > currentTime;
    } catch {
      return false;
    }
  }

  // Helper method to check if admin is authenticated
  isAdminAuthenticated(): boolean {
    const adminToken = localStorage.getItem('adminToken');
    const adminUserStr = localStorage.getItem('adminUser');
    
    if (!adminToken || !adminUserStr) return false;
    
    try {
      // Check token expiration
      const payload = JSON.parse(atob(adminToken.split('.')[1]));
      const currentTime = Date.now() / 1000;
      
      if (payload.exp <= currentTime) return false;
      
      // Check admin role
      const adminUser = JSON.parse(adminUserStr);
      return adminUser.role === 'admin';
    } catch {
      return false;
    }
  }
}

export const apiService = new ApiService();
export default apiService;