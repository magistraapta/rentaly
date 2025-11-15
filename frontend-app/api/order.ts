import { BaseResponse } from "../type/BaseResponse"
import { CreateOrder, Order } from "../type/Order"
import { api } from "./api"
import { BookingResponse } from "../type/Order"

export async function createBooking(carId: number, bookingData: CreateOrder): Promise<BookingResponse> {
    const response = await api.post<BaseResponse<BookingResponse>>(`/v1/bookings/book/${carId}`, bookingData)
    return response.data
}

export async function getOrderByUser(): Promise<Order[]> {
    const response = await api.get<BaseResponse<Order[]>>("/v1/bookings/user/orders")
    return response.data
}

export async function deleteOrder(orderId: number): Promise<Order> {
    const response = await api.delete<Order>(`/v1/bookings/${orderId}`)
    return response
}
