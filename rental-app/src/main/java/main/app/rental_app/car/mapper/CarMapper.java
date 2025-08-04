package main.app.rental_app.car.mapper;

import org.mapstruct.Mapper;

import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.dto.CarDto;

/**
 * CarMapper is a mapper for the Car entity and CarDto
 */
@Mapper(componentModel = "spring")
public interface CarMapper {
    CarDto toDto(Car car);
    Car toEntity(CarDto carDto);
}
