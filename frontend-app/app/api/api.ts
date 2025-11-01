import { AxiosRequestConfig, AxiosResponse } from "axios"
import { apiClient } from "./client"

export const api = {
    get: <T>(url: string, config?: AxiosRequestConfig): Promise<T> =>
        apiClient.get<T>(url, config).then((res: AxiosResponse<T>) => res.data),

    post: <T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> =>
        apiClient.post<T>(url, data, config).then((res: AxiosResponse<T>) => res.data),

    put: <T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> =>
        apiClient.put<T>(url, data, config).then((res: AxiosResponse<T>) => res.data),

    patch: <T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> =>
        apiClient.patch<T>(url, data, config).then((res: AxiosResponse<T>) => res.data),

    delete: <T>(url: string, config?: AxiosRequestConfig): Promise<T> =>
        apiClient.delete<T>(url, config).then((res: AxiosResponse<T>) => res.data),
}

