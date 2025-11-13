export interface PaymentRequest {
    invoiceId: number
    SuccessUrl?: string
    CancelUrl?: string
}

export interface PaymentResponse {
    paymentLinkId: string
    paymentUrl: string
    status: string
    amount: string
    currency: string
    description: string
}