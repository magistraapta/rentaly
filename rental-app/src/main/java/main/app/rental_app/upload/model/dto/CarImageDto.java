package main.app.rental_app.upload.model.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarImageDto {
    @JsonIgnore
    private Long id;
    private String imageUrl;
    @JsonIgnore
    private Instant createdAt;
    @JsonIgnore
    private Instant updatedAt;
}
