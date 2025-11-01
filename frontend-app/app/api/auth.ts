import { LoginRequest } from "../data/auth"
import { api } from "./api"

// Backend response structure matching BaseResponse<T>
interface BaseResponse<T> {
    statusCode: number
    message: string
    data: T
    timestamp?: string
}

interface LoginResponseData {
    accessToken: string
    refreshToken: string
    user: {
        username: string
        email: string
        role: string
    }
}

interface RegisterResponseData {
    email: string
}

interface UserResponseData {
    username: string
    email: string
    role: string
}

export async function login(request: LoginRequest): Promise<LoginResponseData> {
    const response = await api.post<BaseResponse<LoginResponseData>>(
        "/v1/auth/login",
        request
    )
    
    // Store tokens in localStorage
    if (typeof window !== "undefined" && response.data) {
        localStorage.setItem("token", response.data.accessToken)
        localStorage.setItem("refreshToken", response.data.refreshToken)
    }
    
    return response.data
}

export async function register(request: {
    username: string
    email: string
    password: string
}): Promise<RegisterResponseData> {
    const response = await api.post<BaseResponse<RegisterResponseData>>(
        "/v1/auth/register",
        request
    )
    return response.data
}

export async function getCurrentUser(): Promise<UserResponseData> {
    const response = await api.get<BaseResponse<UserResponseData>>("/v1/auth/me")
    return response.data
}

export async function logout(): Promise<void> {
    try {
        await api.post<BaseResponse<void>>("/v1/auth/logout")
    } finally {
        // Clear tokens regardless of API response
        if (typeof window !== "undefined") {
            localStorage.removeItem("token")
            localStorage.removeItem("refreshToken")
        }
    }
}

export async function refreshToken(refreshToken: string): Promise<LoginResponseData> {
    const response = await api.post<BaseResponse<LoginResponseData>>(
        "/v1/auth/refresh-token",
        { refreshToken }
    )
    
    // Update tokens in localStorage
    if (typeof window !== "undefined" && response.data) {
        localStorage.setItem("token", response.data.accessToken)
        localStorage.setItem("refreshToken", response.data.refreshToken)
    }
    
    return response.data
}

