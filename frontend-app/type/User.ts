export interface User {
    id: number
    username: string
    email: string
    password: string
    role: string
    createdAt: Date
    updatedAt: Date
}
export interface UserResponseData {
    username: string
    email: string
    role: string
}