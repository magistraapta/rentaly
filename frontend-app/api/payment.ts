import { api } from "./api"
import { BaseResponse } from "../type/BaseResponse"
import { PaymentRequest, PaymentResponse } from "../type/Payment"

export async function createPayment(paymentRequest: PaymentRequest): Promise<PaymentResponse> {
    const response = await api.post<BaseResponse<PaymentResponse>>("/v1/payment/create", paymentRequest)
    return response.data
}

