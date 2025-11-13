package main.app.rental_app.bookings.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.bookings.model.enums.PaymentStatus;
import main.app.rental_app.bookings.model.enums.RentStatus;
import main.app.rental_app.bookings.repository.InvoiceRepository;
import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.repository.CarRepository;

/**
 * Service to handle automatic expiration of unpaid invoices
 * Runs every minute to check for expired pending invoices
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceExpirationService {

    private final InvoiceRepository invoiceRepository;
    private final CarRepository carRepository;

    /**
     * Scheduled task that runs every minute to check for expired invoices
     * Cancels expired pending invoices and restores car stock
     */
    @Scheduled(fixedRate = 60000) // Run every 60 seconds (1 minute)
    @Transactional
    public void cancelExpiredInvoices() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<Invoices> expiredInvoices = invoiceRepository.findExpiredPendingInvoices(
                PaymentStatus.pending, 
                now
            );

            if (expiredInvoices.isEmpty()) {
                log.debug("No expired invoices found");
                return;
            }

            log.info("Found {} expired invoice(s) to cancel", expiredInvoices.size());

            for (Invoices invoice : expiredInvoices) {
                try {
                    // Update invoice status to cancelled
                    invoice.setStatus(PaymentStatus.cancelled);
                    invoice.setRentStatus(RentStatus.cancelled);
                    invoice.setUpdatedAt(Instant.now());
                    invoiceRepository.save(invoice);

                    // Restore car stock
                    Car car = invoice.getCar();
                    if (car != null) {
                        car.setStock(car.getStock() + 1);
                        carRepository.save(car);
                        log.info("Cancelled invoice ID: {}, restored stock for car ID: {}", 
                            invoice.getId(), car.getId());
                    } else {
                        log.warn("Invoice ID: {} has no associated car, cannot restore stock", invoice.getId());
                    }
                } catch (Exception e) {
                    log.error("Error cancelling invoice ID: {}", invoice.getId(), e);
                    // Continue processing other invoices even if one fails
                }
            }

            log.info("Successfully processed {} expired invoice(s)", expiredInvoices.size());
        } catch (Exception e) {
            log.error("Error in scheduled task to cancel expired invoices", e);
        }
    }
}

