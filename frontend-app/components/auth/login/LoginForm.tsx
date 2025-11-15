"use client"

import { useState } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import { useAuth } from "../../../context/AuthContext"
import { Button } from "../../ui/button"
import { useMutation } from "@tanstack/react-query"

export default function LoginForm() {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const {login} = useAuth()
    const router = useRouter()
    
    const {mutate, isPending, error} = useMutation({
        mutationFn: ({ username, password }: { username: string; password: string }) => 
            login(username, password),
        onSuccess: () => {
            router.push("/")
        },
        onError: (error: Error) => {
            // Error is already handled and thrown by the login function in AuthContext
            console.error("Login failed:", error)
        }
    })

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        mutate({username, password})
    }

    return (
        <div className="flex items-center justify-center min-h-screen">
            <div className="w-full max-w-md p-8 bg-white rounded-lg shadow-md">
                <h1 className="text-2xl font-bold text-center mb-6">Login</h1>
                
                {error && (
                    <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
                        {error.message}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="username" className="block text-sm font-medium mb-2">
                            Username
                        </label>
                        <input
                            id="username"
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="Enter your username"
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium mb-2">
                            Password
                        </label>
                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="Enter your password"
                        />
                    </div>

                    <Button
                        type="submit"
                        disabled={isPending}
                        className="w-full"
                    >
                        {isPending ? "Logging in..." : "Login"}
                    </Button>
                </form>

                <div className="mt-4 text-center text-sm">
                    <span className="text-gray-600">Don&apos;t have an account? </span>
                    <Link href="/register" className="text-blue-600 hover:underline">
                        Register here
                    </Link>
                </div>
            </div>
        </div>
    )
}
