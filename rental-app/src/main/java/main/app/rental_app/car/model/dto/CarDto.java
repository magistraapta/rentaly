package main.app.rental_app.car.model.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.app.rental_app.car.model.enums.CarType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private CarType carType;
    private Integer stock;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;
}