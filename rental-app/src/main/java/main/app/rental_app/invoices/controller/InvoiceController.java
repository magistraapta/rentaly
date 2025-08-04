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

@RestController
@RequestMapping("/v1/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {
    
    private final InvoiceService invoiceService;

    @PostMapping("/book")
    public ResponseEntity<?> bookCar(@RequestBody Invoices invoice) {
        try {
            log.info("Controller:Booking car: {}", invoice);
            return ResponseEntity.ok(invoiceService.createInvoice(invoice));
        } catch (Exception e) {
            log.error("Controller: Error booking car: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        try {
            log.info("Controller: Getting invoice by id: {}", id);
            return ResponseEntity.ok(invoiceService.getInvoiceById(id));
        } catch (Exception e) {
            log.error("Controller: Error getting invoice by id: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllInvoices() {
        try {
            log.info("Controller: Getting all invoices");
            return ResponseEntity.ok(invoiceService.getAllInvoices());
        } catch (Exception e) {
            log.error("Controller: Error getting all invoices: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
