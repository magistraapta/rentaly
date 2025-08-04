package main.app.rental_app.auth.jwt;

import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import main.app.rental_app.user.model.User;
import io.jsonwebtoken.Claims;

public interface JwtService {
    public String generateToken(User user);
    public String generateRefreshToken(User user);
    public String extractUsername(String token);
    public boolean isTokenValid(String token, UserDetails userDetails);
    public String extractRefreshToken(String token);
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver);
}
