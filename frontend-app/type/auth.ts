import { User } from "./User"

export interface LoginRequest {
    username: string
    password: string
}

export interface LoginResponse {
    accessToken: string
    refreshToken: string
    user: User
}

export interface RegisterRequest {
    username: string
    email: string
    password: string
}

export interface RegisterResponseData {
    email: string
}

