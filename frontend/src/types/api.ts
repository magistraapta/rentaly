// API Response types
export interface BaseResponse<T> {
  statusCode: number;
  message: string;
  data: T;
  timestamp: string;
}

// Car related types
export type CarType = 'sedan' | 'suv' | 'truck';

export interface Car {
  name: string;
  description: string;
  price: number;
  carType: CarType;
  carImage: string;
}

export interface CarListResponse extends BaseResponse<Car[]> {}
export interface CarResponse extends BaseResponse<Car> {}

// API Error type
export interface ApiError {
  statusCode: number;
  message: string;
  timestamp: string;
}