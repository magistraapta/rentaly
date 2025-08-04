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
            log.info("Service: Creating invoice: {}", invoice);
            inventoryService.decreaseCarInventory(invoice.getCar().getId());
            return invoiceRepository.save(invoice);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create invoice", e );
        }
    }

    @Override
    public Invoices getInvoiceById(Long id) {
        log.info("Service: Getting invoice by id: {}", id);
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    @Override
    public List<Invoices> getAllInvoices() {
        log.info("Service: Getting all invoices");
        return invoiceRepository.findAll();
    }
}
