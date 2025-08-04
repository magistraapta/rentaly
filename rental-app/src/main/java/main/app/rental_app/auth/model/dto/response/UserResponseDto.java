package main.app.rental_app.auth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String email;
    
    @JsonIgnore
    private String password;
    private String role;
}
