"use client"

import { useEffect, useState } from "react"
import { useParams, useRouter } from "next/navigation"
import { getCarById } from "../../api/car"
import { Car } from "../../type/Car"
import Image from "next/image"
import { Button } from "@/components/ui/button"

export default function CarDetailPage() {
    const params = useParams()
    const router = useRouter()
    const [car, setCar] = useState<Car | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const [imageError, setImageError] = useState(false)

    useEffect(() => {
        const fetchCar = async () => {
            const idParam = params?.id
            const id = Array.isArray(idParam) ? idParam[0] : idParam
            
            if (!id) {
                setError("Car ID is required")
                setLoading(false)
                return
            }

            const carId = Number(id)
            if (isNaN(carId)) {
                setError("Invalid car ID")
                setLoading(false)
                return
            }

            try {
                setLoading(true)
                const data = await getCarById(carId)
                // Add id to car data since backend doesn't return it in CarDto
                setCar({ ...data, id: carId })
            } catch (err) {
                console.error("Error fetching car:", err)
                setError(err instanceof Error ? err.message : "Failed to load car")
            } finally {
                setLoading(false)
            }
        }
        fetchCar()
    }, [params])

    if (loading) {
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

    if (error || !car) {
        return (
            <div className="min-h-screen pt-20 px-6">
                <div className="max-w-7xl mx-auto">
                    <div className="flex flex-col justify-center items-center h-64 gap-4">
                        <p className="text-red-500">Error: {error || "Car not found"}</p>
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
                            <Button 
                                size="lg" 
                                className="w-full"
                                disabled={car.stock !== undefined && car.stock === 0}
                            >
                                {car.stock === 0 ? "Out of Stock" : "Rent This Car"}
                            </Button>
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


