"use client"

import Sidebar from "../components/admin/Sidebar"

export default function Admin() {
    return (
        <div className="flex">
            <Sidebar />
            <main className="flex-1 ml-64 p-8">
                <div className="max-w-7xl mx-auto">
                    <h1 className="text-3xl font-bold mb-6">Admin Dashboard</h1>
                    <p className="text-gray-600">Welcome to the admin panel. Select an option from the sidebar to get started.</p>
                </div>
            </main>
        </div>
    )
}