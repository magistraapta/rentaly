package main.app.rental_app.car.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.upload.model.CarImage;
import main.app.rental_app.upload.model.dto.CarImageDto;

/**
 * CarMapper is a mapper for the Car entity and CarDto
 */
@Mapper(componentModel = "spring")
public interface CarMapper {
    
    default CarDto toDto(Car car) {
        List<CarImageDto> carImageDtos = null;
        if (car.getCarImages() != null) {
            carImageDtos = car.getCarImages().stream()
                    .map(this::toCarImageDto)
                    .collect(Collectors.toList());
        }
        
        return CarDto.builder()
                .id(car.getId())
                .name(car.getName())
                .description(car.getDescription())
                .price(car.getPrice())
                .carType(car.getCarType())
                .carImages(carImageDtos)
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
                .build();
    }
    
    default CarImageDto toCarImageDto(CarImage carImage) {
        if (carImage == null) {
            return null;
        }
        return CarImageDto.builder()
                .id(carImage.getId())
                .imageUrl(carImage.getImageUrl())
                .createdAt(carImage.getCreatedAt())
                .updatedAt(carImage.getUpdatedAt())
                .build();
    }
    
    default String getFirstCarImageUrl(List<CarImage> carImages) {
        if (carImages == null || carImages.isEmpty()) {
            return null;
        }
        return carImages.get(0).getImageUrl();
    }
}
