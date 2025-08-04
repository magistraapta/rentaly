package main.app.rental_app.auth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;

    @JsonProperty("user")
    private UserResponseDto userResponseDto;
}
