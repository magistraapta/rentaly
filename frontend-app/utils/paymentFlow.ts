import { createBooking } from "../api/order"
import { createPayment } from "../api/payment"
import { CreateOrder } from "../type/Order"

/**
 * Complete payment flow for new booking:
 * 1. Create booking/invoice
 * 2. Create Stripe payment link
 * 3. Redirect to Stripe checkout
 */
export async function processPayment(
    carId: number,
    bookingData: CreateOrder,
    onError?: (error: Error) => void
): Promise<void> {
    try {
        // Step 1: Create booking/invoice
        const booking = await createBooking(carId, bookingData)
        
        // Step 2: Create payment link with success/cancel URLs
        const baseUrl = typeof window !== "undefined" ? window.location.origin : "http://localhost:3000"
        const paymentRequest = {
            invoiceId: booking.id,
            SuccessUrl: `${baseUrl}/payment/success?session_id={CHECKOUT_SESSION_ID}`,
            CancelUrl: `${baseUrl}/payment/cancel`,
        }
        
        const paymentResponse = await createPayment(paymentRequest)
        
        // Step 3: Redirect to Stripe checkout
        if (paymentResponse.paymentUrl) {
            window.location.href = paymentResponse.paymentUrl
        } else {
            throw new Error("Payment URL not received")
        }
    } catch (error) {
        console.error("Payment processing error:", error)
        if (onError) {
            onError(error instanceof Error ? error : new Error("Failed to process payment"))
        } else {
            throw error
        }
    }
}

/**
 * Process payment for an existing order/invoice:
 * 1. Create Stripe payment link for existing invoice
 * 2. Redirect to Stripe checkout
 */
export async function processPaymentForOrder(
    invoiceId: number,
    onError?: (error: Error) => void
): Promise<void> {
    try {
        // Create payment link with success/cancel URLs
        const baseUrl = typeof window !== "undefined" ? window.location.origin : "http://localhost:3000"
        const paymentRequest = {
            invoiceId,
            SuccessUrl: `${baseUrl}/payment/success?session_id={CHECKOUT_SESSION_ID}`,
            CancelUrl: `${baseUrl}/payment/cancel`,
        }
        
        const paymentResponse = await createPayment(paymentRequest)
        
        // Redirect to Stripe checkout
        if (paymentResponse.paymentUrl) {
            window.location.href = paymentResponse.paymentUrl
        } else {
            throw new Error("Payment URL not received")
        }
    } catch (error) {
        console.error("Payment processing error:", error)
        if (onError) {
            onError(error instanceof Error ? error : new Error("Failed to process payment"))
        } else {
            throw error
        }
    }
}

