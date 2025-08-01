package main.app.rental_app.car.model;

import lombok.Builder;
import lombok.Data;
import main.app.rental_app.car.model.enums.CarType;

@Data
@Builder
public class Car {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private CarType carType;
}
