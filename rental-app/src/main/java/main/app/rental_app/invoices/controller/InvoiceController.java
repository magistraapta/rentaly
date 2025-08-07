package main.app.rental_app.invoices.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import main.app.rental_app.invoices.model.Invoices;
import main.app.rental_app.invoices.service.InvoiceService;
import main.app.rental_app.shared.BaseResponse;
import java.util.List;

@RestController
@RequestMapping("/v1/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {
    
    private final InvoiceService invoiceService;

    @PostMapping("/book/{carId}")
    public ResponseEntity<BaseResponse<Invoices>> bookCar(@RequestBody Invoices invoice, @PathVariable long carId) {
        try {
            log.info("Controller:Booking car: {}", invoice);
            return invoiceService.createInvoice(invoice, carId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Invoices>> getInvoiceById(@PathVariable Long id) {
        try {
            log.info("Controller: Getting invoice by id: {}", id);
            return invoiceService.getInvoiceById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<Invoices>>> getAllInvoices() {
        try {
            log.info("Controller: Getting all invoices");
            return invoiceService.getAllInvoices();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<List<Invoices>>> getInvoicesByUserId(@PathVariable Long userId) {
        try {
            log.info("Controller: Getting invoices by user id: {}", userId);
            return invoiceService.getInvoicesByUserId(userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<BaseResponse<Invoices>> returnCar(@PathVariable Long id) {
        try {
            log.info("Controller: Returning car for invoice: {}", id);
            return invoiceService.returnCar(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BaseResponse<Invoices>> cancelRental(@PathVariable Long id) {
        try {
            log.info("Controller: Cancelling rental for invoice: {}", id);
            return invoiceService.cancelRental(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}
