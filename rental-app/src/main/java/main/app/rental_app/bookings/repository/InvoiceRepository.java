package main.app.rental_app.bookings.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.bookings.model.enums.PaymentStatus;

public interface InvoiceRepository extends JpaRepository<Invoices, Long> {
    
    List<Invoices> findByStatus(PaymentStatus status);
    
    @Query("SELECT i FROM Invoices i WHERE i.status = :status")
    List<Invoices> findByStatusWithQuery(PaymentStatus status);

    @Query("SELECT i FROM Invoices i WHERE i.user.id = :userId")
    List<Invoices> findByUserId(Long userId);

    @Query("SELECT i FROM Invoices i WHERE i.status = :status AND i.expiredAt IS NOT NULL AND i.expiredAt < :currentTime")
    List<Invoices> findExpiredPendingInvoices(@Param("status") PaymentStatus status, @Param("currentTime") LocalDateTime currentTime);
}
