"use client"

import { useState } from "react"
import { useParams, useRouter } from "next/navigation"
import { getCarById } from "../../../api/car"
import Image from "next/image"
import { Button } from "@/components/ui/button"
import { useAuth } from "../../../context/AuthContext"
import { useQuery, useMutation } from "@tanstack/react-query"
import { processPayment } from "../../../utils/paymentFlow"
import { CreateOrder } from "../../../type/Order"

export default function CarDetailPage() {
    const params = useParams()
    const router = useRouter()
    const { isAuthenticated } = useAuth()
    const [imageError, setImageError] = useState(false)
    const [showBookingForm, setShowBookingForm] = useState(false)
    const [startDate, setStartDate] = useState("")
    const [endDate, setEndDate] = useState("")
    const [paymentError, setPaymentError] = useState<string | null>(null)

    // Extract and validate car ID
    const idParam = params?.id
    const id = Array.isArray(idParam) ? idParam[0] : idParam
    const carId = id ? Number(id) : null

    // Fetch car data using useQuery
    const { data: car, isLoading, isError, error } = useQuery({
        queryKey: ["car", carId],
        queryFn: async () => {
            if (!carId || isNaN(carId)) {
                throw new Error("Invalid car ID")
            }
            const carData = await getCarById(carId)
            // Add id to car data since backend doesn't return it in CarDto
            return { ...carData, id: carId }
        },
        enabled: !!carId && !isNaN(carId),
    })

    // Payment mutation
    const paymentMutation = useMutation({
        mutationFn: async (bookingData: CreateOrder) => {
            if (!carId) throw new Error("Car ID is required")
            await processPayment(carId, bookingData, (error) => {
                setPaymentError(error.message)
            })
        },
        onError: (error: Error) => {
            setPaymentError(error.message || "Failed to process payment")
        },
    })

    const handleRentClick = () => {
        if (!isAuthenticated) {
            router.push("/auth/login")
            return
        }
        setShowBookingForm(true)
    }

    const handleBookingSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        setPaymentError(null)

        if (!startDate || !endDate) {
            setPaymentError("Please select both start and end dates")
            return
        }

        const bookingData: CreateOrder = {
            startDate,
            endDate,
        }

        paymentMutation.mutate(bookingData)
    }

    // Loading state
    if (isLoading) {
        return (
            <div className="min-h-screen pt-20 px-6">
                <div className="max-w-7xl mx-auto">
                    <div className="flex justify-center items-center h-64">
                        <p className="text-gray-500">Loading car details...</p>
                    </div>
                </div>
            </div>
        )
    }

    // Error state
    if (isError || !car) {
        return (
            <div className="min-h-screen pt-20 px-6">
                <div className="max-w-7xl mx-auto">
                    <div className="flex flex-col justify-center items-center h-64 gap-4">
                        <p className="text-red-500">
                            Error: {error instanceof Error ? error.message : "Car not found"}
                        </p>
                        <Button onClick={() => router.push("/cars")}>Back to Cars</Button>
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div className="min-h-screen pt-20 px-6">
            <div className="max-w-7xl mx-auto">
                <Button 
                    variant="outline" 
                    onClick={() => router.back()}
                    className="mb-6"
                >
                    ← Back
                </Button>
                
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    <div className="relative">
                        <Image 
                            src={imageError ? "/not-found.png" : car.imageUrl} 
                            alt={car.name} 
                            width={800} 
                            height={600} 
                            className="rounded-lg w-full h-auto"
                            onError={() => setImageError(true)}
                        />
                        <div className="absolute top-4 left-4 bg-black/70 text-white px-4 py-2 rounded-md">
                            <p className="text-sm font-medium">{car.carType}</p>
                        </div>
                    </div>
                    
                    <div className="space-y-6">
                        <div>
                            <h1 className="text-4xl font-bold mb-4">{car.name || "Car Name"}</h1>
                            <p className="text-xl text-gray-600 mb-2">${car.price || 0}/day</p>
                            {car.stock !== undefined && car.stock !== null && (
                                <div className="flex items-center gap-2">
                                    <span className={`inline-block w-3 h-3 rounded-full ${car.stock > 0 ? 'bg-green-500' : 'bg-red-500'}`}></span>
                                    <p className="text-sm text-gray-500">
                                        {car.stock > 0 
                                            ? `${car.stock} ${car.stock === 1 ? 'car available' : 'cars available'}` 
                                            : 'Out of stock'}
                                    </p>
                                </div>
                            )}
                        </div>
                        
                        {car.description && (
                            <div>
                                <h2 className="text-2xl font-semibold mb-3">Description</h2>
                                <p className="text-gray-700 leading-relaxed">{car.description}</p>
                            </div>
                        )}
                        
                        <div className="pt-4">
                            {!showBookingForm ? (
                                <>
                                    <Button 
                                        size="lg" 
                                        className="w-full"
                                        disabled={!isAuthenticated || (car.stock !== undefined && car.stock === 0) || paymentMutation.isPending}
                                        onClick={handleRentClick}
                                    >
                                        {paymentMutation.isPending
                                            ? "Processing..."
                                            : !isAuthenticated 
                                                ? "Login to Rent This Car" 
                                                : car.stock === 0 
                                                    ? "Out of Stock" 
                                                    : "Rent This Car"}
                                    </Button>
                                    {!isAuthenticated && (
                                        <p className="mt-2 text-sm text-center text-gray-600">
                                            Please <button onClick={() => router.push("/auth/login")} className="text-blue-600 hover:underline font-medium">login</button> to rent this car
                                        </p>
                                    )}
                                </>
                            ) : (
                                <form onSubmit={handleBookingSubmit} className="space-y-4">
                                    <div>
                                        <label htmlFor="startDate" className="block text-sm font-medium text-gray-700 mb-1">
                                            Start Date
                                        </label>
                                        <input
                                            type="date"
                                            id="startDate"
                                            value={startDate}
                                            onChange={(e) => setStartDate(e.target.value)}
                                            min={new Date().toISOString().split("T")[0]}
                                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                            required
                                        />
                                    </div>
                                    <div>
                                        <label htmlFor="endDate" className="block text-sm font-medium text-gray-700 mb-1">
                                            End Date
                                        </label>
                                        <input
                                            type="date"
                                            id="endDate"
                                            value={endDate}
                                            onChange={(e) => setEndDate(e.target.value)}
                                            min={startDate || new Date().toISOString().split("T")[0]}
                                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                            required
                                        />
                                    </div>
                                    {paymentError && (
                                        <div className="p-3 bg-red-50 border border-red-200 rounded-lg">
                                            <p className="text-sm text-red-600">{paymentError}</p>
                                        </div>
                                    )}
                                    <div className="flex gap-3">
                                        <Button
                                            type="button"
                                            variant="outline"
                                            className="flex-1"
                                            onClick={() => {
                                                setShowBookingForm(false)
                                                setPaymentError(null)
                                            }}
                                            disabled={paymentMutation.isPending}
                                        >
                                            Cancel
                                        </Button>
                                        <Button
                                            type="submit"
                                            size="lg"
                                            className="flex-1"
                                            disabled={paymentMutation.isPending}
                                        >
                                            {paymentMutation.isPending ? "Processing..." : "Proceed to Payment"}
                                        </Button>
                                    </div>
                                </form>
                            )}
                        </div>
                        
                        <div className="pt-4 border-t">
                            <h3 className="text-lg font-semibold mb-4">Car Details</h3>
                            <div className="space-y-3 text-sm">
                                {car.carType && (
                                    <div className="flex justify-between items-center py-2 border-b border-gray-100">
                                        <span className="text-gray-600">Type:</span>
                                        <span className="font-medium capitalize">{String(car.carType).toLowerCase()}</span>
                                    </div>
                                )}
                                <div className="flex justify-between items-center py-2 border-b border-gray-100">
                                    <span className="text-gray-600">Price:</span>
                                    <span className="font-medium text-lg">${car.price || 0}/day</span>
                                </div>
                                {car.stock !== undefined && car.stock !== null && (
                                    <div className="flex justify-between items-center py-2 border-b border-gray-100">
                                        <span className="text-gray-600">Availability:</span>
                                        <span className={`font-medium ${car.stock > 0 ? 'text-green-600' : 'text-red-600'}`}>
                                            {car.stock > 0 ? 'In Stock' : 'Out of Stock'}
                                        </span>
                                    </div>
                                )}
                                {car.createdAt && (
                                    <div className="flex justify-between items-center py-2 border-b border-gray-100">
                                        <span className="text-gray-600">Added:</span>
                                        <span className="font-medium text-xs">
                                            {new Date(car.createdAt).toLocaleDateString()}
                                        </span>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}


