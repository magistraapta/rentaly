package main.app.rental_app.auth.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import main.app.rental_app.auth.model.dto.request.LoginRequest;
import main.app.rental_app.auth.model.dto.request.RefreshTokenDto;
import main.app.rental_app.auth.model.dto.request.RegisterRequest;

public interface AuthService {
    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> register(RegisterRequest registerRequest);

    ResponseEntity<?> refreshToken(RefreshTokenDto dto);

    ResponseEntity<?> logout(HttpServletRequest request);
}
