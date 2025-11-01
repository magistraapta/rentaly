export interface LoginResponseData {
    accessToken: string
    refreshToken: string
    user: {
        username: string
        email: string
        role: string
    }
}


export interface LoginRequest {
    username: string
    password: string
}