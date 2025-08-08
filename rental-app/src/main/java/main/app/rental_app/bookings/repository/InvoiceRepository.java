package main.app.rental_app.bookings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.bookings.model.enums.PaymentStatus;

public interface InvoiceRepository extends JpaRepository<Invoices, Long> {
    
    List<Invoices> findByStatus(PaymentStatus status);
    
    @Query("SELECT i FROM Invoices i WHERE i.status = :status")
    List<Invoices> findByStatusWithQuery(PaymentStatus status);

    @Query("SELECT i FROM Invoices i WHERE i.user.id = :userId")
    List<Invoices> findByUserId(Long userId);
}
