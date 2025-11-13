package main.app.rental_app.bookings.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.bookings.model.dto.CreateInvoicesDto;
import main.app.rental_app.bookings.model.dto.ResponseInvoiceDto;
import main.app.rental_app.bookings.model.enums.PaymentStatus;
import main.app.rental_app.bookings.model.enums.RentStatus;
import main.app.rental_app.bookings.model.mapper.InvoiceMapper;
import main.app.rental_app.bookings.repository.InvoiceRepository;
import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.repository.CarRepository;
import main.app.rental_app.shared.BaseResponse;
import main.app.rental_app.user.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    
    private final InvoiceRepository invoiceRepository;
    private final CarRepository carRepository;

    @Value("${invoice.expiration.minutes:15}")
    private int invoiceExpirationMinutes;

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<Invoices>> createInvoice(CreateInvoicesDto invoiceDto, long carId) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.error(HttpStatus.UNAUTHORIZED, "User not authenticated"));
            }
            
            User user = (User) authentication.getPrincipal();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.error(HttpStatus.UNAUTHORIZED, "User not found"));
            }
            
            Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
            
            if (car.getPrice() == null) {
                return ResponseEntity.badRequest()
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST, "Car price is not set"));
            }

            // Validate car availability
            if (car.getStock() <= 0) {
                return ResponseEntity.badRequest()
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST, "Car is not available"));
            }

            // Validate dates
            if (invoiceDto.getStartDate() == null || invoiceDto.getEndDate() == null) {
                return ResponseEntity.badRequest()
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST, "Start date and end date are required"));
            }

            if (invoiceDto.getStartDate().isAfter(invoiceDto.getEndDate())) {
                return ResponseEntity.badRequest()
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST, "Start date cannot be after end date"));
            }

            log.info("Creating invoice for car ID: {}, user: {}, startDate: {}, endDate: {}", 
                carId, user.getId(), invoiceDto.getStartDate(), invoiceDto.getEndDate());

            
            Invoices newInvoice = Invoices.builder()
                .startDate(invoiceDto.getStartDate().atStartOfDay())
                .endDate(invoiceDto.getEndDate().atTime(23, 59, 59))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            // Calculate total price
            int totalPrice = car.getPrice() * (invoiceDto.getEndDate().getDayOfYear() - invoiceDto.getStartDate().getDayOfYear());
            
            // Set additional fields that are not in DTO
            newInvoice.setUser(user);
            newInvoice.setCar(car);
            newInvoice.setStatus(PaymentStatus.pending);
            newInvoice.setRentStatus(RentStatus.rented);
            newInvoice.setExpiredAt(LocalDateTime.now().plusMinutes(invoiceExpirationMinutes));
            newInvoice.setTotalPrice(totalPrice);
            
            log.info("Invoice will expire at: {}", newInvoice.getExpiredAt());

            car.setStock(car.getStock() - 1);
            carRepository.save(car);

            log.info("Saving invoice to database");
            invoiceRepository.save(newInvoice);

            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Invoice created", newInvoice));
        } catch (Exception e) {
            log.error("Failed to create invoice: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create invoice: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<BaseResponse<ResponseInvoiceDto>> getInvoiceById(Long id) {
        Invoices invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found"));
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Invoice found", InvoiceMapper.toResponseInvoiceDto(invoice)));
    }

    @Override
    public ResponseEntity<BaseResponse<List<ResponseInvoiceDto>>> getAllInvoices() {
        List<Invoices> invoices = invoiceRepository.findAll();

        List<ResponseInvoiceDto> responseInvoices = invoices.stream()
            .map(InvoiceMapper::toResponseInvoiceDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Invoices found", responseInvoices));
    }   

    @Override
    public ResponseEntity<BaseResponse<List<ResponseInvoiceDto>>> getOrderByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED, "User not authenticated"));
        }

        User user = (User) authentication.getPrincipal();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED, "User not found"));
        }

        List<Invoices> invoices = invoiceRepository.findByUserId(user.getId());
        List<ResponseInvoiceDto> responseInvoices = invoices.stream()
            .map(InvoiceMapper::toResponseInvoiceDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Orders found", responseInvoices));
    }

    @Override
    public ResponseEntity<BaseResponse<List<ResponseInvoiceDto>>> getInvoicesByUserId(Long userId) {
        List<Invoices> invoices = invoiceRepository.findByUserId(userId);
        List<ResponseInvoiceDto> responseInvoices = invoices.stream()
            .map(InvoiceMapper::toResponseInvoiceDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Invoices found", responseInvoices));
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
            
            invoice.getCar().setStock(invoice.getCar().getStock() + 1);
            carRepository.save(invoice.getCar());
            
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
            
            invoice.getCar().setStock(invoice.getCar().getStock() + 1);
            carRepository.save(invoice.getCar());
            
            invoiceRepository.save(invoice);
            
            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Rental cancelled successfully", invoice));
        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel rental", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<Void>> deleteInvoice(Long invoiceId) {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED, "User not authenticated"));
        }
        
        User user = (User) authentication.getPrincipal();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED, "User not found"));
        }
        
        // Find invoice
        Invoices invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        // Check if the invoice belongs to the authenticated user
        if (invoice.getUser() == null || !invoice.getUser().getId().equals(user.getId())) {
            log.warn("User ID: {} attempted to delete invoice ID: {} owned by user ID: {}", 
                user.getId(), invoiceId, invoice.getUser() != null ? invoice.getUser().getId() : "null");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.error(HttpStatus.FORBIDDEN, "You are not authorized to delete this invoice"));
        }
        
        // Restore car stock if invoice is not already cancelled
        if (invoice.getStatus() != PaymentStatus.cancelled && invoice.getCar() != null) {
            Car car = invoice.getCar();
            car.setStock(car.getStock() + 1);
            carRepository.save(car);
            log.info("Restored stock for car ID: {} after deleting invoice ID: {}", car.getId(), invoiceId);
        }
        
        invoiceRepository.delete(invoice);
        log.info("Invoice ID: {} deleted successfully by user ID: {}", invoiceId, user.getId());
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Invoice deleted successfully"));
    }   
}
