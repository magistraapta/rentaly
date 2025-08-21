package main.app.rental_app.car.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.app.rental_app.car.model.enums.CarType;
import main.app.rental_app.upload.model.dto.CarImageDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private String name;
    private String description;
    private Integer price;
    private CarType carType;
    private List<CarImageDto> carImages;
}