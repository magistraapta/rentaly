import Link from "next/link";
import { Button } from "@/components/ui/button";

export default function Navbar() {
    
    return (
        <div className="absolute top-0 left-0 right-0 z-50 flex justify-between items-center p-6">
            <h1 className="text-2xl font-bold text-white">Rentaly</h1>
            <div className="flex items-center gap-4 text-white">
                <Button>
                    <Link href="/login">Login</Link>
                </Button>
                <Button variant="secondary">
                    <Link href="/register">Register</Link>
                </Button>
            </div>
        </div>
    )
}