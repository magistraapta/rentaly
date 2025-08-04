package main.app.rental_app.auth.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterRequest extends AuthRequestDto {
    
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(max = 255, message = "Email must be less than 255 characters")
    @NotBlank(message = "Email is required")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    
}
