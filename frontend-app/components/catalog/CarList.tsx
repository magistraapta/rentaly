import { Car } from "@/app/type/Car";
import Card from "./Card";

export default function CarList ({ cars }: { cars: Car[] }) {
    return (
      <div className="m-6">
        <h2 className="text-3xl font-bold mb-6">Our Top Picks</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {cars.map((car) => (
            <Card key={car.id} car={car} />
          ))}
        </div>
      </div>
    )
  }