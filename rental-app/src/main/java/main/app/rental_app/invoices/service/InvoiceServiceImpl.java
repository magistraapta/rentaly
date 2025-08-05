package main.app.rental_app.invoices.service;

import org.springframework.stereotype.Service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.invoices.model.Invoices;
import main.app.rental_app.invoices.repository.InvoiceRepository;
import main.app.rental_app.inventory.service.InventoryService;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    
    private final InvoiceRepository invoiceRepository;
    private final InventoryService inventoryService;

    @Override
    public Invoices createInvoice(Invoices invoice) {
        try {
            inventoryService.decreaseCarInventory(invoice.getCar().getId());
            return invoiceRepository.save(invoice);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create invoice", e );
        }
    }

    @Override
    public Invoices getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    @Override
    public List<Invoices> getAllInvoices() {
        return invoiceRepository.findAll();
    }
}
