package main.app.rental_app.car.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.enums.CarType;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByName(String name);

    @Query("SELECT c FROM Car c WHERE c.carType = :carType")
    List<Car> findByCarType(CarType carType);
}
