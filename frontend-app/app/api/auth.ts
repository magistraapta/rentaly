import { LoginRequest, RegisterRequest } from "../data/auth"

export async function login(request: LoginRequest) {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/v1/auth/login`, {
        method: "POST",
        body: JSON.stringify(request),
    })
    if (!response.ok) {
        throw new Error("Failed to login")
    }
    return response.json()
}

export async function register(request: RegisterRequest) {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/v1/auth/register`, {
        method: "POST",
        body: JSON.stringify(request),
    })
    if (!response.ok) {
        throw new Error("Failed to register")
    }
    return response.json()
}