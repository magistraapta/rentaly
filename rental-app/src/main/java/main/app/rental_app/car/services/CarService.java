package main.app.rental_app.car.services;

import org.springframework.http.ResponseEntity;

import main.app.rental_app.shared.BaseResponse;
import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.car.model.enums.CarType;

import java.util.List;

public interface CarService {
    ResponseEntity<BaseResponse<CarDto>> getCarById(Long carId) throws ResourceNotFoundException;
    ResponseEntity<BaseResponse<CarDto>> getCarByName(String carName) throws ResourceNotFoundException;
    ResponseEntity<BaseResponse<List<CarDto>>> getCarsByType(CarType carType) throws ResourceNotFoundException;
    ResponseEntity<BaseResponse<CarDto>> addCar(CarDto carDto) throws ResourceNotFoundException;
}
