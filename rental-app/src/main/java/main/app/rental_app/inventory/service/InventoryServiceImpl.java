package main.app.rental_app.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.exc.BadRequestException;
import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.inventory.model.Inventory;
import main.app.rental_app.inventory.repository.InventoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public void decreaseCarInventory(Long carId) {
        log.info("Attempting to decrease inventory for car id: {}", carId);
        
        // Optional: Pre-check for better user feedback (not required for safety)
        inventoryRepository.findByCarId(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for car id: " + carId));
        
        // Use atomic operation to decrease quantity (this is the safety mechanism)
        int updatedRows = inventoryRepository.decreaseQuantityByCarId(carId);
        
        if (updatedRows == 0) {
            // Double-check if it's out of stock
            Inventory inventory = inventoryRepository.findByCarId(carId)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for car id: " + carId));
            
            if (inventory.getQuantity() <= 0) {
                throw new BadRequestException("Car is out of stock (quantity: " + inventory.getQuantity() + ")");
            }
            
            // This shouldn't happen, but just in case
            throw new BadRequestException("Failed to decrease inventory for car id: " + carId);
        }
        
        log.info("Successfully decreased inventory for car id: {}", carId);
    }

    @Override
    @Transactional
    public void increaseCarInventory(Long carId) {
        log.info("Attempting to increase inventory for car id: {}", carId);
        
        // Use atomic operation to increase quantity
        int updatedRows = inventoryRepository.increaseQuantityByCarId(carId);
        
        if (updatedRows == 0) {
            throw new ResourceNotFoundException("Inventory not found for car id: " + carId);
        }
        
        log.info("Successfully increased inventory for car id: {}", carId);
    }

    @Override
    public void checkCarAvailability(Long carId) {
        log.info("Checking availability for car id: {}", carId);
        
        Inventory inventory = inventoryRepository.findByCarId(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for car id: " + carId));
        
        if (inventory.getQuantity() <= 0) {
            throw new BadRequestException("Car is out of stock (quantity: " + inventory.getQuantity() + ")");
        }
        
        log.info("Car id: {} is available with quantity: {}", carId, inventory.getQuantity());
    }
}
