export interface Order {
    id: number
    car: string
    user: string
    startDate: Date
    endDate: Date
    totalPrice: number
    status: string
    createdAt: Date
    updatedAt: Date
}