package main.app.rental_app.bookings.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.shared.BaseResponse;

public interface InvoiceService {
    ResponseEntity<BaseResponse<Invoices>> createInvoice(Invoices invoice, long carId);
    ResponseEntity<BaseResponse<Invoices>> getInvoiceById(Long id);
    ResponseEntity<BaseResponse<List<Invoices>>> getAllInvoices();
    ResponseEntity<BaseResponse<List<Invoices>>> getInvoicesByUserId(Long userId);
    ResponseEntity<BaseResponse<Invoices>> returnCar(Long invoiceId);
    ResponseEntity<BaseResponse<Invoices>> cancelRental(Long invoiceId);
}
