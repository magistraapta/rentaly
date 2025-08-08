package main.app.rental_app.car.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.upload.model.CarImage;

import java.util.List;

/**
 * CarMapper is a mapper for the Car entity and CarDto
 */
@Mapper(componentModel = "spring")
public interface CarMapper {
    
    @Mapping(target = "carImage", expression = "java(getFirstCarImageUrl(car.getCarImages()))")
    CarDto toDto(Car car);
    
    Car toEntity(CarDto carDto);
    
    default String getFirstCarImageUrl(List<CarImage> carImages) {
        if (carImages == null || carImages.isEmpty()) {
            return null;
        }
        return carImages.get(0).getImageUrl();
    }
}
