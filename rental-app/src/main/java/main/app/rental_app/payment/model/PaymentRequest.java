package main.app.rental_app.payment.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    private String SuccessUrl;
    private String CancelUrl;
}
