package main.app.rental_app.car.model.dto;


import lombok.Builder;
import lombok.Data;
import main.app.rental_app.car.model.enums.CarType;

@Data
@Builder
public class CarDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private CarType carType;
}