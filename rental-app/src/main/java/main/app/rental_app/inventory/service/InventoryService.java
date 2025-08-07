package main.app.rental_app.inventory.service;

/**
 * InventoryService is a service for the inventory module
 */
public interface InventoryService {
    void decreaseCarInventory(Long carId);
    void increaseCarInventory(Long carId);
    void checkCarAvailability(Long carId);
}
