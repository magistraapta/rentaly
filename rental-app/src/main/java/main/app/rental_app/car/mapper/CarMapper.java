package main.app.rental_app.car.mapper;

import org.mapstruct.Mapper;

import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.dto.CarDto;

/**
 * CarMapper is a mapper for the Car entity and CarDto
 */
@Mapper(componentModel = "spring")
public interface CarMapper {
    
    default CarDto toDto(Car car) {
        return CarDto.builder()
                .name(car.getName())
                .description(car.getDescription())
                .price(car.getPrice())
                .carType(car.getCarType())
                .stock(car.getStock())
                .imageUrl(car.getImageUrl())
                .createdAt(car.getCreatedAt())
                .updatedAt(car.getUpdatedAt())
                .build();
    }
    
    default Car toEntity(CarDto carDto) {
        return Car.builder()
                .name(carDto.getName())
                .description(carDto.getDescription())
                .price(carDto.getPrice())
                .carType(carDto.getCarType())
                .stock(carDto.getStock())
                .imageUrl(carDto.getImageUrl())
                .build();
    }
}
