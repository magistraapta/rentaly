package main.app.rental_app.payment.service;

import main.app.rental_app.payment.model.PaymentRequest;
import main.app.rental_app.payment.model.PaymentResponse;

public interface PaymentService {
    PaymentRequest createPayment(PaymentRequest paymentRequest);
    PaymentResponse getPayment(String paymentLinkId);
}
