package main.app.rental_app.car.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import main.app.rental_app.car.mapper.CarMapper;
import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.car.model.enums.CarType;
import main.app.rental_app.car.repository.CarRepository;
import main.app.rental_app.exc.CarNotFoundException;
import main.app.rental_app.shared.BaseResponse;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public BaseResponse<List<CarDto>> getAllCars() {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            throw new CarNotFoundException("No cars found");
        }
        List<CarDto> carDtos = cars.stream()
            .map(carMapper::toDto)
            .collect(Collectors.toList());
        return BaseResponse.success(HttpStatus.OK, "Cars found", carDtos);
    }

    @Override
    public BaseResponse<CarDto> getCarById(Long carId) {
        Car car = carRepository.findById(carId)
            .orElseThrow(() -> new CarNotFoundException("Car not found"));

        if (car == null) {
            throw new CarNotFoundException("Car not found");
        }
        return BaseResponse.success(HttpStatus.OK, "Car found", carMapper.toDto(car));
    }

    @Override
    public BaseResponse<CarDto> getCarByName(String carName) {
        Car car = carRepository.findByName(carName);
        if (car == null) {
            throw new CarNotFoundException("Car not found");
        }   

        return BaseResponse.success(HttpStatus.OK, "Car found", carMapper.toDto(car));
    }

    @Override
    public BaseResponse<List<CarDto>> getCarsByType(CarType carType) {

        List<Car> cars = carRepository.findByCarType(carType);
        if (cars.isEmpty()) {
            throw new CarNotFoundException("No cars found for type: " + carType);
        }
        return BaseResponse.success(HttpStatus.OK, "Cars found", cars.stream().map(carMapper::toDto).collect(Collectors.toList()));
    }

    @Override
    public BaseResponse<CarDto> addCar(CarDto carDto) {
        Car car = carMapper.toEntity(carDto);
        carRepository.save(car);
        return BaseResponse.success(HttpStatus.OK, "Car added", carMapper.toDto(car));
    }
}
