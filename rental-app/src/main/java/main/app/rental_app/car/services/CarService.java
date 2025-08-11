package main.app.rental_app.car.services;

import java.util.List;

import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.car.model.enums.CarType;
import main.app.rental_app.shared.BaseResponse;

public interface CarService {
    BaseResponse<List<CarDto>> getAllCars();
    BaseResponse<CarDto> getCarById(Long carId);
    BaseResponse<CarDto> getCarByName(String carName);
    BaseResponse<List<CarDto>> getCarsByType(CarType carType);
    BaseResponse<CarDto> addCar(CarDto carDto);
}
    