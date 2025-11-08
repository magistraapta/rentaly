import OrderCard from "../../components/order/OrderCard"
import { Order } from "../type/Order"
    
export default function OrderPage() {
    return (
        <div className="min-h-screen pt-20 px-6">
            <h1 className="text-4xl font-bold">My Orders</h1>
            {/* Order list */}
            <div>
                <p>Data is dummy</p>
                {orders.map((order) => (
                    <OrderCard key={order.id} order={order} />
                ))}
            </div>

        </div>
    )
}




const orders: Order[] = [
    {
        id: 1,
        car: "Car 1",
        user: "User 1",
        startDate: new Date(),
        endDate: new Date(),
        totalPrice: 100,
        status: "Pending",
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        id: 2,
        car: "Car 2",
        user: "User 2",
        startDate: new Date(),
        endDate: new Date(),
        totalPrice: 200,
        status: "Pending",
        createdAt: new Date(),
        updatedAt: new Date()
    }
]
