"use client"

import OrderCard from "../../components/order/OrderCard"
import { useQuery } from "@tanstack/react-query"
import { getOrderByUser } from "../../api/order"
import { Button } from "@/components/ui/button"
import Link from "next/link"

export default function OrderPage() {
    const { data, isLoading, isError, error } = useQuery({
        queryKey: ["orders"],
        queryFn: getOrderByUser
    })

    if (isLoading) {
        return <div>Loading...</div>
    }

    if (isError) {
        return <div>Error: {error instanceof Error ? error.message : "An unknown error occurred"}</div>
    }

    return (
        <div className="min-h-screen pt-20 px-6">
            <h1 className="text-4xl font-bold mb-6">My Orders</h1>
            <Button asChild className="mb-6">
                <Link href="/">
                    Back to Home
                </Link>
            </Button>
            {/* Order list */}
            <div className="space-y-4">
                {data && data.length > 0 ? (
                    data.map((order) => (
                        <OrderCard key={order.id} order={order} />
                    ))
                ) : (
                    <div className="text-center py-12 text-gray-500">
                        <p>No orders found</p>
                    </div>
                )}
            </div>

        </div>
    )
}