package main.app.rental_app.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import main.app.rental_app.inventory.model.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    /**
     * Find inventory by car ID
     */
    Optional<Inventory> findByCarId(Long carId);
    
    /**
     * Decrease inventory quantity atomically
     */
    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = i.quantity - 1 WHERE i.car.id = :carId AND i.quantity > 0")
    int decreaseQuantityByCarId(@Param("carId") Long carId);
    
    /**
     * Increase inventory quantity atomically
     */
    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = i.quantity + 1 WHERE i.car.id = :carId")
    int increaseQuantityByCarId(@Param("carId") Long carId);
}
