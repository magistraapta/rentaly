import { User } from "../type/User"

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

