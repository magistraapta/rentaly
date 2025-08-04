package main.app.rental_app.auth.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenDto {
    private String refreshToken;
}
