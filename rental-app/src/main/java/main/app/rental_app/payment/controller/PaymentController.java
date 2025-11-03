package main.app.rental_app.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.app.rental_app.payment.model.PaymentRequest;
import main.app.rental_app.payment.model.PaymentResponse;
import main.app.rental_app.payment.service.PaymentService;
import main.app.rental_app.shared.BaseResponse;

@RestController
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<PaymentResponse>> createPayment(
        @Valid @RequestBody PaymentRequest paymentRequest) {
            PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest);

            return ResponseEntity.ok(BaseResponse.<PaymentResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Payment created successfully")
                .data(paymentResponse)
                .build());
    }

    @GetMapping("/{paymentLinkId}")
    public ResponseEntity<BaseResponse<PaymentResponse>> getPayment(
        @PathVariable String paymentLinkId) {
            PaymentResponse paymentResponse = paymentService.getPayment(paymentLinkId);

            return ResponseEntity.ok(BaseResponse.<PaymentResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Payment retrieved successfully")
                .data(paymentResponse)
                .build());
    }

    @PostMapping("/webhook")
    public ResponseEntity<BaseResponse<Void>> handleWebhook(
        @RequestBody String payload,
        @RequestHeader("Stripe-Signature") String signature) {
            paymentService.handleWebhook(payload, signature);

            return ResponseEntity.ok(BaseResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Webhook received successfully")
                .build());
    }
}
