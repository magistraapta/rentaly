"use client"

import { useState } from "react";
import Link from "next/link";
import { Car } from "@/app/type/Car";
import { Button } from "@/components/ui/button";
import Image from "next/image";

interface CardProps {
    car: Car;
}

export default function Card ({ car }: CardProps) {
    const [imageError, setImageError] = useState(false);

    return (
        <div className="bg-white p-4 rounded-lg shadow-md">
            <Link href={`/cars/${car.id}`} className="block">
                <div className="relative">
                    <Image 
                        src={imageError ? "/not-found.png" : car.imageUrl} 
                        alt={car.name} 
                        width={500} 
                        height={100} 
                        className="rounded-lg"
                        onError={() => setImageError(true)}
                    />
                    <div className="absolute top-2 left-2 bg-black/70 text-white px-3 py-1 rounded-md">
                        <p className="text-sm font-medium">{car.carType}</p>
                    </div>
                </div>
                
                <div className="my-4">
                    <h3 className="text-xl font-bold mb-2">{car.name}</h3>
                    <div>
                        <p className="text-sm text-gray-500 mb-2">{car.description}</p>
                        <p className="text-sm text-gray-500 mb-2">${car.price}/day</p>
                    </div>
                </div>
            </Link>
            <Button className="w-full">Rent</Button>
        </div>
    )
}