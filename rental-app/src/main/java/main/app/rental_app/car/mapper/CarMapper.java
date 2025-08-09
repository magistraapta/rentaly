package main.app.rental_app.car.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.upload.model.CarImage;

import java.util.List;

/**
 * CarMapper is a mapper for the Car entity and CarDto
 */
@Mapper(componentModel = "spring")
public interface CarMapper {
    
    default CarDto toDto(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .name(car.getName())
                .description(car.getDescription())
                .price(car.getPrice())
                .carType(car.getCarType())
                .carImage(getFirstCarImageUrl(car.getCarImages()))
                .build();
    }
    
    default Car toEntity(CarDto carDto) {
        return Car.builder()
                .id(carDto.getId())
                .name(carDto.getName())
                .description(carDto.getDescription())
                .price(carDto.getPrice())
                .carType(carDto.getCarType())
                .build();
    }
    
    default String getFirstCarImageUrl(List<CarImage> carImages) {
        if (carImages == null || carImages.isEmpty()) {
            return null;
        }
        return carImages.get(0).getImageUrl();
    }
}
