package main.app.rental_app.invoices.service;

import main.app.rental_app.invoices.model.Invoices;
import java.util.List;

public interface InvoiceService {
    Invoices createInvoice(Invoices invoice);
    Invoices getInvoiceById(Long id);
    List<Invoices> getAllInvoices();
}
