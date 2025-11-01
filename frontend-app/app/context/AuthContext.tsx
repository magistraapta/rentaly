import { createContext, useContext, useEffect, useState } from "react";
import { User } from "../type/User";
import axios from "axios";

interface AuthContextType {
    user: User | null
    loading: boolean
    login: (email: string, password: string) => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [user, setUser] = useState<User | null>(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const user = await axios.get<User>("/api/user")
                setUser(user.data)
            } catch {
                setUser(null)
                setLoading(false)
            } finally {
                setLoading(false)
            }
        }
        fetchUser()
    }, [])

    const login = async (email: string, password: string) => {
        try {
            await axios.post("/v1/auth/login", { email, password })
            const user = await axios.get<User>("/api/user")
            setUser(user.data)
        } catch {
            throw new Error("Failed to login")
        }
    }

    return (
        <AuthContext.Provider value={{ user, loading, login }}>
          {children}
        </AuthContext.Provider>
      );
}

export const useAuth = () => {
    const context = useContext(AuthContext)
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider")
    }
    return context
}


