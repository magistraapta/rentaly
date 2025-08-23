package main.app.rental_app.car.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.enums.CarType;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByName(String name);

    @Query("SELECT c FROM Car c WHERE c.carType = :carType")
    List<Car> findByCarType(CarType carType);
    
    @Query("SELECT c FROM Car c LEFT JOIN FETCH c.carImages WHERE c.id = :id")
    Optional<Car> findByIdWithImages(@Param("id") Long id);
}
