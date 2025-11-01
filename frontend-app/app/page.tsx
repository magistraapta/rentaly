import { Button } from "@/components/ui/button";
import { Car } from "./type/Car";
import Navbar from "./components/Navbar/Navbar";
import Link from "next/link";
import { getAllCars } from "./api/car";
import CarList from "./components/catalog/CarList";
  
export default async function Home() {
  const cars: Car[] = await getAllCars()  
  return (
    <div className="bg-zinc-50">
      <Navbar />
      <Hero />
      <CarList cars={cars} />
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
            <Button variant={"outline"}>
              <Link href="/cars">Get Started</Link>
            </Button>
          </div>
        </div>

      </div>
    </div>
  )
}



