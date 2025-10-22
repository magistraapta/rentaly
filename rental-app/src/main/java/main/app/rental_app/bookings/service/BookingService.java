package main.app.rental_app.bookings.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.bookings.model.dto.CreateInvoicesDto;
import main.app.rental_app.bookings.model.dto.ResponseInvoiceDto;
import main.app.rental_app.shared.BaseResponse;

public interface BookingService {
    ResponseEntity<BaseResponse<Invoices>> createInvoice(CreateInvoicesDto invoiceDto, long carId);
    ResponseEntity<BaseResponse<ResponseInvoiceDto>> getInvoiceById(Long id);
    ResponseEntity<BaseResponse<List<ResponseInvoiceDto>>> getAllInvoices();
    ResponseEntity<BaseResponse<List<ResponseInvoiceDto>>> getInvoicesByUserId(Long userId);
    ResponseEntity<BaseResponse<Invoices>> returnCar(Long invoiceId);
    ResponseEntity<BaseResponse<Invoices>> cancelRental(Long invoiceId);
}
