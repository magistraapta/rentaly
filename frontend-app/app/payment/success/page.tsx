"use client"

import Link from "next/link"
import { useSearchParams } from "next/navigation"
import Navbar from "@/components/Navbar/Navbar"
import { Button } from "@/components/ui/button"

export default function PaymentSuccessPage() {
    const searchParams = useSearchParams()
    
    const sessionId = searchParams.get("session_id")
    const paymentIntent = searchParams.get("payment_intent")
    const amount = searchParams.get("amount")
    
    const paymentDetails = {
        sessionId: sessionId || undefined,
        paymentIntent: paymentIntent || undefined,
        amount: amount || undefined,
    }

    return (
        <div className="min-h-screen bg-zinc-50">
            <Navbar />
            <div className="flex min-h-screen items-center justify-center px-6 pt-20">
                <div className="w-full max-w-2xl">
                    <div className="rounded-2xl border border-gray-200 bg-white p-8 shadow-lg dark:border-neutral-800 dark:bg-neutral-900 md:p-12">
                        {/* Success Icon */}
                        <div className="mx-auto mb-6 flex h-20 w-20 items-center justify-center rounded-full bg-emerald-100 dark:bg-emerald-900/20">
                            <svg
                                className="h-12 w-12 text-emerald-600 dark:text-emerald-400"
                                fill="none"
                                stroke="currentColor"
                                viewBox="0 0 24 24"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth={2}
                                    d="M5 13l4 4L19 7"
                                />
                            </svg>
                        </div>

                        {/* Success Message */}
                        <div className="text-center">
                            <h1 className="mb-3 text-3xl font-bold text-gray-900 dark:text-gray-100 md:text-4xl">
                                Payment Successful!
                            </h1>
                            <p className="mb-8 text-lg text-gray-600 dark:text-gray-400">
                                Thank you for your payment. Your rental has been confirmed.
                            </p>
                        </div>

                        {/* Payment Details */}
                        {(paymentDetails.sessionId || paymentDetails.paymentIntent || paymentDetails.amount) && (
                            <div className="mb-8 rounded-xl border border-gray-200 bg-gray-50 p-6 dark:border-neutral-700 dark:bg-neutral-800">
                                <h2 className="mb-4 text-lg font-semibold text-gray-900 dark:text-gray-100">
                                    Payment Details
                                </h2>
                                <div className="space-y-3">
                                    {paymentDetails.sessionId && (
                                        <div className="flex justify-between">
                                            <span className="text-sm text-gray-600 dark:text-gray-400">Session ID:</span>
                                            <span className="text-sm font-mono text-gray-900 dark:text-gray-100">
                                                {paymentDetails.sessionId}
                                            </span>
                                        </div>
                                    )}
                                    {paymentDetails.paymentIntent && (
                                        <div className="flex justify-between">
                                            <span className="text-sm text-gray-600 dark:text-gray-400">Payment Intent:</span>
                                            <span className="text-sm font-mono text-gray-900 dark:text-gray-100">
                                                {paymentDetails.paymentIntent}
                                            </span>
                                        </div>
                                    )}
                                    {paymentDetails.amount && (
                                        <div className="flex justify-between">
                                            <span className="text-sm text-gray-600 dark:text-gray-400">Amount:</span>
                                            <span className="text-sm font-semibold text-gray-900 dark:text-gray-100">
                                                {new Intl.NumberFormat(undefined, {
                                                    style: "currency",
                                                    currency: "USD",
                                                }).format(parseFloat(paymentDetails.amount) / 100)}
                                            </span>
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}

                        {/* Info Box */}
                        <div className="mb-8 rounded-lg bg-blue-50 p-4 dark:bg-blue-900/20">
                            <div className="flex">
                                <div className="shrink-0">
                                    <svg
                                        className="h-5 w-5 text-blue-400"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                    >
                                        <path
                                            fillRule="evenodd"
                                            d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
                                            clipRule="evenodd"
                                        />
                                    </svg>
                                </div>
                                <div className="ml-3">
                                    <p className="text-sm text-blue-700 dark:text-blue-300">
                                        A confirmation email has been sent to your email address. You can view your order details in the orders section.
                                    </p>
                                </div>
                            </div>
                        </div>

                        {/* Action Buttons */}
                        <div className="flex flex-col gap-4 sm:flex-row sm:justify-center">
                            <Button
                                asChild
                                className="w-full sm:w-auto"
                            >
                                <Link href="/order">View My Orders</Link>
                            </Button>
                            <Button
                                asChild
                                variant="outline"
                                className="w-full sm:w-auto"
                            >
                                <Link href="/">Back to Home</Link>
                            </Button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

