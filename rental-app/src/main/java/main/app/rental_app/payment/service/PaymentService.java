package main.app.rental_app.payment.service;

import main.app.rental_app.payment.model.PaymentRequest;
import main.app.rental_app.payment.model.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest paymentRequest);
    PaymentResponse getPayment(String paymentLinkId);
    void handleWebhook(String payload, String signature);
}
