export default function Loading() {
    return (
        <div className="flex items-center justify-center min-h-screen">
            <div className="w-full max-w-md p-8 bg-white rounded-lg shadow-md">
                <h1 className="text-2xl font-bold text-center mb-6">Login</h1>
                <div className="flex items-center justify-center">
                    <div className="w-16 h-16 border-t-4 border-b-4 border-gray-900 rounded-full animate-spin"></div>
                </div>
            </div>
        </div>
    )
}