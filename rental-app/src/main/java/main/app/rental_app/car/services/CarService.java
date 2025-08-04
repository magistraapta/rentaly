package main.app.rental_app.car.services;

import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.car.model.enums.CarType;

import java.util.List;

public interface CarService {
    CarDto getCarById(Long carId) throws ResourceNotFoundException;
    CarDto getCarByName(String carName) throws ResourceNotFoundException;
    List<CarDto> getCarsByType(CarType carType) throws ResourceNotFoundException;
}
