package main.app.rental_app.inventory.model;

import lombok.Builder;
import lombok.Data;
import main.app.rental_app.car.model.Car;

@Data
@Builder
public class Inventory {
    private Long id;
    private Car car;
    private Integer quantity;
}
