import { getAllCars } from "../api/car"
import Card from "../components/catalog/Card"
import { Car } from "../type/Car"
import Navbar from "../components/Navbar/Navbar"


export default async function Cars() {
    const cars: Car[] = await getAllCars()

    return (
        <div>
            <Navbar />
            <div className="min-h-screen pt-20 px-6">
                <h2 className="text-3xl font-bold mb-6">Our Top Picks</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {cars.map((car) => (
                        <Card key={car.id} car={car} />
                    ))}
                </div>
            </div>
        </div>
        
    )
}