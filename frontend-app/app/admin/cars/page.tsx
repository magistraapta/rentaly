"use client"

import Sidebar from "../../components/admin/Sidebar"
import { getAllCars } from "../../api/car"
import { Car } from "../../type/Car"
import { Button } from "@/components/ui/button"
import Image from "next/image"
import Link from "next/link"
import { useQuery } from "@tanstack/react-query"
import { useState } from "react"

export default function AdminCarsPage() {
    const [imageError, setImageError] = useState(false)
    const { data: cars, isLoading, isError, error } = useQuery({
        queryKey: ["cars"],
        queryFn: () => getAllCars()
    })  

    const handleDelete = async (id: number) => {
        if (!confirm("Are you sure you want to delete this car?")) {
            return
        }
        // TODO: Implement delete API call
        console.log("Delete car:", id)
    }

    return (
        <div className="flex">
            <Sidebar />
            <main className="flex-1 ml-64 p-8">
                <div className="max-w-7xl mx-auto">
                    <div className="flex justify-between items-center mb-6">
                        <h1 className="text-3xl font-bold">Cars Management</h1>
                        <Button>
                            Add New Car
                        </Button>
                    </div>

                    {isLoading ? (
                        <div className="flex justify-center items-center h-64">
                            <p className="text-gray-500">Loading cars...</p>
                        </div>
                    ) : isError ? (
                        <div className="flex justify-center items-center h-64">
                            <p className="text-red-500">Error: {error instanceof Error ? error.message : "Failed to load cars"}</p>
                        </div>
                    ) : (
                        <div className="bg-white rounded-lg shadow-md overflow-hidden">
                            <table className="min-w-full divide-y divide-gray-200">
                                <thead className="bg-gray-50">
                                    <tr>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Image
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Name
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Type
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Price/Day
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Stock
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Description
                                        </th>
                                        <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Actions
                                        </th>
                                    </tr>
                                </thead>
                                <tbody className="bg-white divide-y divide-gray-200">
                                    {cars?.length === 0 ? (
                                        <tr>
                                            <td colSpan={7} className="px-6 py-4 text-center text-gray-500">
                                                No cars found
                                            </td>
                                        </tr>
                                    ) : (
                                        cars?.map((car: Car) => (
                                            <tr key={car.id} className="hover:bg-gray-50">
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="relative w-16 h-16 rounded-md overflow-hidden">
                                                    <Image 
                                                        src={imageError ? "/not-found.png" : car.imageUrl} 
                                                        alt={car.name} 
                                                        width={500} 
                                                        height={100} 
                                                        className="rounded-lg"
                                                        onError={() => setImageError(true)}
                                                    />
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="text-sm font-medium text-gray-900">
                                                        <Link href={`/cars/${car.id}`} className="hover:text-blue-600">
                                                            {car.name}
                                                        </Link>
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                                                        {car.carType}
                                                    </span>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="text-sm text-gray-900">${car.price}</div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                                                        car.stock > 0 
                                                            ? "bg-green-100 text-green-800" 
                                                            : "bg-red-100 text-red-800"
                                                    }`}>
                                                        {car.stock}
                                                    </span>
                                                </td>
                                                <td className="px-6 py-4">
                                                    <div className="text-sm text-gray-500 max-w-xs truncate">
                                                        {car.description}
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                                    <div className="flex justify-end gap-2">
                                                        <Button
                                                            variant="outline"
                                                            size="sm"
                                                            onClick={() => {/* TODO: Edit car */}}
                                                        >
                                                            Edit
                                                        </Button>
                                                        <Button
                                                            variant="destructive"
                                                            size="sm"
                                                            onClick={() => handleDelete(car.id)}
                                                        >
                                                            Delete
                                                        </Button>
                                                    </div>
                                                </td>
                                            </tr>
                                        ))
                                    )}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </main>
        </div>
    )
}

