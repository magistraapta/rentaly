import LoginForm from "../components/login/LoginForm"

export default function Login() {
    return (
        <div className="min-h-screen flex items-center justify-center bg-zinc-50">
            <div className="w-full max-w-sm bg-white p-6 rounded-lg shadow-md">
                <h1 className="text-2xl font-semibold mb-6 text-center">Login</h1>
                <LoginForm />
            </div>
        </div>
    )
}