"use client"

import { createContext, useContext, useEffect, useState } from "react"
import { User } from "../type/User"
import { getCurrentUser, login as loginApi, logout as logoutApi } from "../api/auth"

interface AuthContextType {
    user: User | null
    loading: boolean
    login: (username: string, password: string) => Promise<void>
    logout: () => Promise<void>
    isAuthenticated: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [user, setUser] = useState<User | null>(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const token = localStorage.getItem("token")
                if (!token) {
                    setLoading(false)
                    return
                }
                
                const userData = await getCurrentUser()
                setUser({
                    id: 0, // Backend doesn't return id in UserResponseDto
                    username: userData.username,
                    email: userData.email,
                    password: "",
                    role: userData.role,
                    createdAt: new Date(),
                    updatedAt: new Date(),
                })
            } catch (error) {
                console.error("Failed to fetch user:", error)
                setUser(null)
                // Clear invalid/expired token on 400 or 401 errors
                if (typeof window !== "undefined") {
                    localStorage.removeItem("token")
                    localStorage.removeItem("refreshToken")
                }
            } finally {
                setLoading(false)
            }
        }
        fetchUser()
    }, [])

    const login = async (username: string, password: string) => {
        try {
            const response = await loginApi({ username, password })
            
            // Update user state
            setUser({
                id: 0,
                username: response.user.username,
                email: response.user.email,
                password: "",
                role: response.user.role,
                createdAt: new Date(),
                updatedAt: new Date(),
            })
        } catch (error) {
            console.error("Login error:", error)
            const errorMessage = error && typeof error === 'object' && 'response' in error 
                ? (error as { response?: { data?: { message?: string } } }).response?.data?.message
                : undefined
            throw new Error(errorMessage || "Failed to login")
        }
    }

    const logout = async () => {
        try {
            await logoutApi()
        } catch (error) {
            console.error("Logout error:", error)
        } finally {
            setUser(null)
        }
    }

    return (
        <AuthContext.Provider
            value={{
                user,
                loading,
                login,
                logout,
                isAuthenticated: !!user,
            }}
        >
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => {
    const context = useContext(AuthContext)
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider")
    }
    return context
}

