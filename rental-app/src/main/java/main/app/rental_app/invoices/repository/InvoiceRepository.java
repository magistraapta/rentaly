package main.app.rental_app.invoices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import main.app.rental_app.invoices.model.Invoices;
import main.app.rental_app.invoices.model.enums.InvoiceStatus;

public interface InvoiceRepository extends JpaRepository<Invoices, Long> {
    
    List<Invoices> findByStatus(InvoiceStatus status);
    
    @Query("SELECT i FROM Invoices i WHERE i.status = :status")
    List<Invoices> findByStatusWithQuery(InvoiceStatus status);
}
