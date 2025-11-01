"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { useAuth } from "../../context/AuthContext"
import { Button } from "@/components/ui/button"

interface SidebarItem {
    href: string
    label: string
    icon?: React.ReactNode
}

const sidebarItems: SidebarItem[] = [
    { href: "/admin/cars", label: "Cars" },
    { href: "/admin/users", label: "Users" },
    { href: "/admin/bookings", label: "Bookings" },
    { href: "/admin/payments", label: "Payments" },
    { href: "/admin/reports", label: "Reports" },
    { href: "/admin/settings", label: "Settings" },
]

export default function Sidebar() {
    const pathname = usePathname()
    const { user, logout } = useAuth()

    const handleLogout = async () => {
        await logout()
        window.location.href = "/login"
    }

    return (
        <div className="fixed left-0 top-0 h-screen w-64 bg-gray-900 text-white shadow-lg">
            <div className="flex flex-col h-full">
                {/* Header */}
                <div className="p-6 border-b border-gray-700">
                    <Link href="/admin">
                        <h2 className="text-2xl font-bold">Admin Panel</h2>
                    </Link>
                    {user && (
                        <p className="text-sm text-gray-400 mt-2">
                            {user.username}
                        </p>
                    )}
                </div>

                {/* Navigation */}
                <nav className="flex-1 p-4 overflow-y-auto">
                    <ul className="space-y-2">
                        {sidebarItems.map((item) => {
                            const isActive = pathname === item.href || pathname?.startsWith(item.href + "/")
                            return (
                                <li key={item.href}>
                                    <Link
                                        href={item.href}
                                        className={`
                                            flex items-center px-4 py-3 rounded-lg transition-colors
                                            ${isActive
                                                ? "bg-blue-600 text-white"
                                                : "text-gray-300 hover:bg-gray-800 hover:text-white"
                                            }
                                        `}
                                    >
                                        {item.icon && <span className="mr-3">{item.icon}</span>}
                                        <span>{item.label}</span>
                                    </Link>
                                </li>
                            )
                        })}
                    </ul>
                </nav>

                {/* Footer with Logout */}
                <div className="p-4 border-t border-gray-700">
                    <Button
                        variant="destructive"
                        className="w-full"
                        onClick={handleLogout}
                    >
                        Logout
                    </Button>
                </div>
            </div>
        </div>
    )
}
