package main.app.rental_app.payment.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.bookings.model.enums.PaymentStatus;
import main.app.rental_app.bookings.repository.InvoiceRepository;
import main.app.rental_app.payment.model.PaymentRequest;
import main.app.rental_app.payment.model.PaymentResponse;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        log.info("Creating payment");

        Invoices invoice = invoiceRepository.findById(paymentRequest.getInvoiceId())
            .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (invoice.getStatus() != PaymentStatus.pending) {
            throw new RuntimeException("Invoice is not pending");
        }

        try {
            Price price = Price.create(
                com.stripe.param.PriceCreateParams.builder()
                    .setCurrency("usd")
                    .setUnitAmount(invoice.getTotalPrice() * 100L)
                    .setProductData(
                        com.stripe.param.PriceCreateParams.ProductData.builder()
                            .setName("Invoice " + invoice.getId())
                            .build()
                    )
                .build()
            );

            List<PaymentLinkCreateParams.LineItem> lineItems = new ArrayList<>();
            lineItems.add(
                PaymentLinkCreateParams.LineItem.builder()
                    .setPrice(price.getId())
                    .setQuantity(1L)
                    .build()
            );
            
            PaymentLinkCreateParams params = PaymentLinkCreateParams.builder()
                .addLineItem(lineItems.get(0))
                .putMetadata("invoice_id", invoice.getId().toString())
                .setAfterCompletion(
                    PaymentLinkCreateParams.AfterCompletion.builder()
                        .setRedirect(
                            PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                .setUrl(paymentRequest.getSuccessUrl())
                                .build()
                        )
                        .build()
                )
                .build();

            PaymentLink paymentLink = PaymentLink.create(params);

            invoice.setStatus(PaymentStatus.pending);
            invoiceRepository.save(invoice);

            return PaymentResponse.builder()
                .paymentLinkId(paymentLink.getId())
                .paymentUrl(paymentLink.getUrl())
                .status(paymentLink.getActive() ? "active" : "inactive")
                .amount(String.valueOf(invoice.getTotalPrice()))
                .currency("usd")
                .description("Invoice " + invoice.getId())
                .build();

        } catch (StripeException e) {
            log.error("Failed to create payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create payment: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse getPayment(String paymentLinkId) {
        log.info("Getting payment");

        try {
            PaymentLink paymentLink = PaymentLink.retrieve(paymentLinkId);
            
            // Get amount from invoice via metadata
            String invoiceId = paymentLink.getMetadata() != null 
                ? paymentLink.getMetadata().get("invoice_id") 
                : null;
            
            String amount = "0";
            String currency = paymentLink.getCurrency() != null ? paymentLink.getCurrency() : "usd";
            
            if (invoiceId != null) {
                Invoices invoice = invoiceRepository.findById(Long.parseLong(invoiceId))
                    .orElse(null);
                if (invoice != null) {
                    amount = String.valueOf(invoice.getTotalPrice());
                }
            }

            return PaymentResponse.builder()
                .paymentLinkId(paymentLink.getId())
                .paymentUrl(paymentLink.getUrl())
                .status(paymentLink.getActive() ? "active" : "inactive")
                .amount(amount)
                .currency(currency)
                .description("payment link for invoice " + paymentLinkId)
                .build();

        } catch (StripeException e) {
            log.error("Failed to get payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get payment: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handleWebhook(String payload, String signature) {
        // Webhook handling implementation can be added here
        log.info("Webhook received");
    }
}
