package main.app.rental_app.car.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.car.repository.CarRepository;
import main.app.rental_app.car.mapper.CarMapper;
import main.app.rental_app.car.model.enums.CarType;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDto getCarById(Long carId) throws ResourceNotFoundException {
        try {
            Car car = carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException("Car not found"));
            if (car == null) {
                throw new ResourceNotFoundException("Car not found");
            }
            return carMapper.toDto(car);
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage());
        }
    }

    @Override
    public CarDto getCarByName(String carName) throws ResourceNotFoundException {
        try {
            Car car = carRepository.findByName(carName);
            if (car == null) {
                throw new ResourceNotFoundException("Car not found");
            }   

            return carMapper.toDto(car);
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage());
        }
    }

    @Override
    public List<CarDto> getCarsByType(CarType carType) throws ResourceNotFoundException {
        try {
            List<Car> cars = carRepository.findByCarType(carType);
            if (cars.isEmpty()) {
                throw new ResourceNotFoundException("No cars found for type: " + carType);
            }
            return cars.stream().map(carMapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResourceNotFoundException("No cars found for type: " + carType);
        }
    }
}
