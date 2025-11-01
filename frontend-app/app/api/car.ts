import { Car } from "../type/Car"
import { api } from "./api"
import { BaseResponse } from "../type/BaseResponse"

export async function getAllCars(): Promise<Car[]> {
    const response = await api.get<BaseResponse<Car[]>>("/v1/cars")
    return response.data
}

export async function getCarById(id: number): Promise<Car> {
    const response = await api.get<BaseResponse<Car>>(`/v1/cars/${id}`)
    return response.data
}