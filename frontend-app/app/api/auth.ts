import { LoginRequest } from "../data/auth"
import { api } from "./api"
import { BaseResponse } from "../type/BaseResponse"
import { LoginResponseData } from "../type/Login"
import { UserResponseData } from "../type/User"

// Backend response structure matching BaseResponse<T>



interface RegisterResponseData {
    email: string
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
    // Check if token exists before making request
    if (typeof window !== "undefined") {
        const token = localStorage.getItem("token")
        if (!token) {
            throw new Error("No authentication token found")
        }
    }
    
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

