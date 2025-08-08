package main.app.rental_app.upload.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarImageDto {
    private Long id;
    private String imageUrl;
}
