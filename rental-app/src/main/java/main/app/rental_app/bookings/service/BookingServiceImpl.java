package main.app.rental_app.bookings.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.inventory.service.InventoryService;
import main.app.rental_app.shared.BaseResponse;
import main.app.rental_app.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import main.app.rental_app.car.repository.CarRepository;
import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.bookings.model.enums.PaymentStatus;
import main.app.rental_app.bookings.model.enums.RentStatus;
import main.app.rental_app.bookings.repository.InvoiceRepository;
import main.app.rental_app.car.model.Car;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    
    private final InvoiceRepository invoiceRepository;
    private final InventoryService inventoryService;
    private final CarRepository carRepository;
    
    @Override
    @Transactional
    public ResponseEntity<BaseResponse<Invoices>> createInvoice(Invoices invoice, long carId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            
            Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));

            try {
                inventoryService.checkCarAvailability(carId);
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST, "Car is not available: " + e.getMessage()));
            }
            
            Invoices newInvoice = Invoices.builder()
                .user(user)
                .car(car)
                .status(PaymentStatus.pending)
                .rentStatus(RentStatus.rented)
                .startDate(invoice.getStartDate())
                .endDate(invoice.getEndDate())
                .totalPrice(car.getPrice())
                .build();

            inventoryService.decreaseCarInventory(car.getId());

            invoiceRepository.save(newInvoice);

            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Invoice created", newInvoice));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create invoice", e );
        }
    }

    @Override
    public ResponseEntity<BaseResponse<Invoices>> getInvoiceById(Long id) {
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Invoice found", invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"))));
    }

    @Override
    public ResponseEntity<BaseResponse<List<Invoices>>> getAllInvoices() {
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Invoices found", invoiceRepository.findAll()));
    }

    @Override
    public ResponseEntity<BaseResponse<List<Invoices>>> getInvoicesByUserId(Long userId) {
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Invoices found", invoiceRepository.findByUserId(userId)));
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<Invoices>> returnCar(Long invoiceId) {
        try {
            Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
            
            if (invoice.getRentStatus() != RentStatus.rented) {
                return ResponseEntity.badRequest()
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST, "Car is not currently rented"));
            }
            
            invoice.setRentStatus(RentStatus.returned);
            invoice.setReturnedAt(Timestamp.valueOf(LocalDateTime.now()));
            
            // Increase car inventory when returned
            inventoryService.increaseCarInventory(invoice.getCar().getId());
            
            invoiceRepository.save(invoice);
            
            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Car returned successfully", invoice));
        } catch (Exception e) {
            throw new RuntimeException("Failed to return car", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<Invoices>> cancelRental(Long invoiceId) {
        try {
            Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
            
            if (invoice.getRentStatus() != RentStatus.rented) {
                return ResponseEntity.badRequest()
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST, "Rental cannot be cancelled"));
            }
            
            invoice.setRentStatus(RentStatus.cancelled);
            
            // Increase car inventory when cancelled
            inventoryService.increaseCarInventory(invoice.getCar().getId());
            
            invoiceRepository.save(invoice);
            
            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Rental cancelled successfully", invoice));
        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel rental", e);
        }
    }
}
