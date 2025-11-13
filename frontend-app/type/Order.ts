export interface CarDto {
    id: number
    name: string
    description?: string
    price: number
    carType: string
    imageUrl?: string
}

export interface UserDto {
    id: number
    username: string
    email: string
}

export interface Order {
    id: number
    car: CarDto
    user: UserDto
    startTime: string // ISO date string from backend
    endTime: string // ISO date string from backend
    totalPrice: number
    status: string
    rentStatus?: string
    createdTime?: string
    updatedTime?: string
}

export interface CreateOrder {
    startDate: string
    endDate: string
}

export interface BookingResponse {
    id: number
    startDate: string
    endDate: string
    totalPrice: number
    status: string
    rentStatus: string
    createdTime: string
    updatedTime: string
}

