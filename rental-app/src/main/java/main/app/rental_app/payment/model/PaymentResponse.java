package main.app.rental_app.payment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String paymentLinkId;
    private String paymentUrl;
    private String status;
    private String amount;
    private String currency;
    private String description;
}
