"use client"

import Link from "next/link";
import { Button } from "@/components/ui/button";
import { useAuth } from "../../context/AuthContext";

export default function Navbar() {
    const { isAuthenticated, user, logout, loading } = useAuth();

    const handleLogout = async () => {
        await logout();
    };

    if (loading) {
        return (
            <div className="absolute top-0 left-0 right-0 z-50 flex justify-between items-center p-6">
                <h1 className="text-2xl font-bold text-black drop-shadow-[0_2px_4px_rgba(255,255,255,0.8)]">
                    <Link href="/">Rentaly</Link>
                </h1>
                <div className="flex items-center gap-4 text-white">
                    <span>Loading...</span>
                </div>
            </div>
        );
    }

    return (
        <div className="absolute top-0 left-0 right-0 z-50 flex justify-between items-center p-6">
            <h1 className="text-2xl font-bold text-black drop-shadow-[0_2px_4px_rgba(255,255,255,0.8)]">
                <Link href="/">Rentaly</Link>
            </h1>
            <div className="flex items-center gap-4 text-white">
                {isAuthenticated && user ? (
                    <>
                        <span className="text-sm">Welcome, {user.username}</span>
                        <Button variant="secondary" onClick={handleLogout}>
                            Logout
                        </Button>
                    </>
                ) : (
                    <>
                        <Button>
                            <Link href="/login">Login</Link>
                        </Button>
                        <Button variant="secondary">
                            <Link href="/register">Register</Link>
                        </Button>
                    </>
                )}
            </div>
        </div>
    );
}