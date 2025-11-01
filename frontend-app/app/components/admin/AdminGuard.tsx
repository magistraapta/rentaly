"use client"

import { useEffect } from "react"
import { useRouter } from "next/navigation"
import { useAuth } from "../../context/AuthContext"
import { Button } from "@/components/ui/button"

interface AdminGuardProps {
    children: React.ReactNode
}

export default function AdminGuard({ children }: AdminGuardProps) {
    const { user, loading, isAuthenticated } = useAuth()
    const router = useRouter()

    useEffect(() => {
        if (!loading) {
            if (!isAuthenticated || !user) {
                router.push("/login")
                return
            }

            if (user.role !== "admin" && user.role !== "ADMIN") {
                router.push("/login")
                return
            }
        }
    }, [user, loading, isAuthenticated, router])

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-center">
                    <p className="text-gray-500">Loading...</p>
                </div>
            </div>
        )
    }

    if (!isAuthenticated || !user) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-center">
                    <p className="text-red-500 mb-4">You must be logged in to access this page.</p>
                    <Button onClick={() => router.push("/login")}>Go to Login</Button>
                </div>
            </div>
        )
    }

    if (user.role !== "admin" && user.role !== "ADMIN") {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-center">
                    <p className="text-red-500 mb-4">Access denied. Admin privileges required.</p>
                    <Button onClick={() => router.push("/")}>Go to Home</Button>
                </div>
            </div>
        )
    }

    return <>{children}</>
}

