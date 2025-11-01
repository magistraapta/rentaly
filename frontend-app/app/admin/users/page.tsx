import Sidebar from "@/app/components/admin/Sidebar";

export default function AdminUsersPage() {
    return (
        <div className="flex">
            <Sidebar />
            <main className="flex-1 ml-64 p-8">
                <div className="max-w-7xl mx-auto">
                    <h1 className="text-3xl font-bold">Users</h1>
                </div>
            </main>
        </div>
    )
}
