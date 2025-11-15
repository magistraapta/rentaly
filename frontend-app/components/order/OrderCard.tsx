"use client"

import { useState } from "react"
import { Order } from "@/type/Order"
import { Button } from "@/components/ui/button"
import { processPaymentForOrder } from "@/utils/paymentFlow"
import { TrashIcon } from "lucide-react"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { deleteOrder } from "@/api/order"
import { toast } from "sonner"

export default function OrderCard({ order }: { order: Order }) {
    const [isProcessing, setIsProcessing] = useState(false)
    const [paymentError, setPaymentError] = useState<string | null>(null)
    const queryClient = useQueryClient()

    const statusStyles: Record<string, string> = {
        pending: "bg-amber-50 text-amber-700 ring-amber-200",
        paid: "bg-emerald-50 text-emerald-700 ring-emerald-200",
        cancelled: "bg-rose-50 text-rose-700 ring-rose-200",
    }

    const deleteMutation = useMutation({
        mutationFn: async () => {
            await deleteOrder(order.id)
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["orders"] })
            toast.success("Order deleted successfully")
        },
        onError: (error: Error) => {
            toast.error(error.message || "Failed to delete order")
        }
    })

    const normalizedStatus = order.status?.toLowerCase() || ""
    const statusClass = statusStyles[order.status] || statusStyles[normalizedStatus] || statusStyles.Default
    const dateFormatter = new Intl.DateTimeFormat(undefined, { year: 'numeric', month: 'short', day: '2-digit' })
    const currencyFormatter = new Intl.NumberFormat(undefined, { style: 'currency', currency: 'USD' })

    const startDate = order.startTime ? new Date(order.startTime) : new Date()
    const endDate = order.endTime ? new Date(order.endTime) : new Date()

    const isPending = order.status?.toLowerCase() === "pending" || order.status === "PENDING"
    const isCancelled = order.status?.toLowerCase() === "cancelled" || order.status === "CANCELLED"

    const handleDelete = () => {
        if (confirm("Are you sure you want to delete this order?")) {
            deleteMutation.mutate()
        }
    }

    const handlePayment = async () => {
        setIsProcessing(true)
        setPaymentError(null)
        
        try {
            await processPaymentForOrder(order.id, (err) => {
                setPaymentError(err.message)
                setIsProcessing(false)
            })
        } catch (err) {
            setPaymentError(err instanceof Error ? err.message : "Failed to process payment")
            setIsProcessing(false)
        }
    }
    

    return (
        <div key={order.id} className="rounded-xl border border-gray-200 mb-4 bg-white p-5 shadow-sm transition hover:shadow-md dark:border-neutral-800 dark:bg-neutral-900">
            <div className="flex items-start justify-between gap-3">
                <div>
                    <h2 className="text-xl font-semibold tracking-tight text-gray-900 dark:text-gray-100">{order.car?.name || 'Unknown Car'}</h2>
                    <p className="mt-1 text-sm text-gray-500">Order #{order.id}</p>
                </div>
                <span className={`inline-flex items-center rounded-full px-2.5 py-1 text-xs font-medium ring-1 ring-inset ${statusClass}`}>
                    {order.status}
                </span>
            </div>

            <div className="mt-4 grid grid-cols-1 gap-4 sm:grid-cols-3">
                <div className="rounded-lg bg-gray-50 p-3 dark:bg-neutral-800">
                    <p className="text-xs uppercase tracking-wide text-gray-500">Start date</p>
                    <p className="mt-1 text-sm font-medium text-gray-900 dark:text-gray-100">{dateFormatter.format(startDate)}</p>
                </div>
                <div className="rounded-lg bg-gray-50 p-3 dark:bg-neutral-800">
                    <p className="text-xs uppercase tracking-wide text-gray-500">End date</p>
                    <p className="mt-1 text-sm font-medium text-gray-900 dark:text-gray-100">{dateFormatter.format(endDate)}</p>
                </div>
                <div className="rounded-lg bg-gray-50 p-3 dark:bg-neutral-800">
                    <p className="text-xs uppercase tracking-wide text-gray-500">Total</p>
                    <p className="mt-1 text-lg font-semibold text-gray-900 dark:text-gray-100">{currencyFormatter.format(order.totalPrice)}</p>
                </div>
            </div>

            {paymentError && (
                <div className="mt-4 p-3 bg-red-50 border border-red-200 rounded-lg">
                    <p className="text-sm text-red-600">{paymentError}</p>
                </div>
            )}

            <div className="mt-4 flex items-center justify-end gap-3">
                {isPending && (
                    <Button
                        onClick={handlePayment}
                        disabled={isProcessing}
                        className="inline-flex items-center rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        {isProcessing ? "Processing..." : "Pay Now"}
                    </Button>
                )}
                <button className="inline-flex items-center rounded-lg bg-gray-900 px-3 py-2 text-sm font-medium text-white hover:bg-black/90 dark:bg-white dark:text-black dark:hover:bg-white/90">
                    View Details
                </button>
                {isCancelled && (
                    <Button 
                        variant="destructive"
                        onClick={handleDelete}
                        disabled={deleteMutation.isPending}
                    >
                        <TrashIcon className="size-4" />
                        {deleteMutation.isPending ? "Deleting..." : "Delete"}
                    </Button>
                )}
            </div>
        </div>
    )
}

