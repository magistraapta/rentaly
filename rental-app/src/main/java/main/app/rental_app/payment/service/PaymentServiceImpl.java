package main.app.rental_app.payment.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.payment.model.PaymentRequest;
import main.app.rental_app.payment.model.PaymentResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    
    @Override
    public PaymentRequest createPayment(PaymentRequest paymentRequest) {
        log.info("Creating payment");
        return paymentRequest;
    }

    @Override
    public PaymentResponse getPayment(String paymentLinkId) {
        log.info("Getting payment");
        return null;
    }
}
