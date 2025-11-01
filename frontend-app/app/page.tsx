import { Button } from "@/components/ui/button";
import Card from "./components/catalog/Card";
import { Car } from "./type/Car";
import { cars } from "./data/car";
import Navbar from "./components/Navbar/Navbar";
  
export default function Home() {
  return (
    <div className="bg-zinc-50">
      <Navbar />
      <Hero />
      <Catalog cars={cars} />
    </div>
  );
}


function Hero () {
  return (
    <div className="relative min-h-screen w-full flex items-center justify-center bg-cover bg-center bg-no-repeat"
      style={{
        backgroundImage: "url('/hero-image.png')"
      }}
    >
      <div className="absolute inset-0 bg-black/40"></div>
      
      {/* Content */}
      <div className="relative z-10 text-center text-white">
        <div className="flex flex-col items-center justify-center">
          <h1 className="text-6xl font-bold drop-shadow-lg">Rental Car Never Been Easier</h1>
        </div>
        <div className="flex flex-col items-center justify-center gap-8">
          <p className="text-lg text-zinc-50">
            Find the perfect car for your next trip with our easy to use rental car booking system.
          </p>
          <div className="flex items-center gap-4 text-black">
            <Button variant={"outline"}>Get Started</Button>
          </div>
        </div>

      </div>
    </div>
  )
}

function Catalog ({ cars }: { cars: Car[] }) {
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

