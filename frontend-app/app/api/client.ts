import axios, { AxiosError, InternalAxiosRequestConfig } from "axios"

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    },
    timeout: 10000,
})

// Request interceptor to add auth token
apiClient.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        if (typeof window !== "undefined") {
            const token = localStorage.getItem("token")
            if (token && config.headers) {
                config.headers.Authorization = `Bearer ${token}`
            }
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// Response interceptor for error handling
apiClient.interceptors.response.use(
    (response) => response,
    (error: AxiosError) => {
        const status = error.response?.status
        if (status === 401 || status === 400) {
            // Handle unauthorized or bad request (invalid/expired token) - clear token and redirect to login
            if (typeof window !== "undefined") {
                localStorage.removeItem("token")
                localStorage.removeItem("refreshToken")
                // Only redirect if we're not already on the login page
                if (window.location.pathname !== "/login") {
                    window.location.href = "/login"
                }
            }
        }
        return Promise.reject(error)
    }
)

