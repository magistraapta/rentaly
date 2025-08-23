package main.app.rental_app.car.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.app.rental_app.car.model.enums.CarType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarRequest {
    private String name;
    private String description;
    private Integer price;
    private CarType carType;
    private MultipartFile[] images;
}
