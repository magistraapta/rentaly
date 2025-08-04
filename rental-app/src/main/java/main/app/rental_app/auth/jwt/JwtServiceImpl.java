package main.app.rental_app.auth.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.user.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private Long expiration;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    // Test method to verify secret key
    public void testSecretKey() {
        try {
            Key key = getSignInKey();
            log.info("Secret key test successful - key algorithm: {}", key.getAlgorithm());
        } catch (Exception e) {
            log.error("Secret key test failed: {}", e.getMessage());
        }
    }

    @Override
    public String generateToken(User user) {
        try {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Error generating JWT token: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String generateRefreshToken(User user) {
        try {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Error generating refresh token: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String extractUsername(String token) {
        try {
            return extractClaims(token, Claims::getSubject);
        } catch (Exception e) {
            log.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username != null && username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String extractRefreshToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true; // Consider expired if we can't check
        }
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            log.debug("Attempting to parse token: {}", token.substring(0, Math.min(20, token.length())) + "...");
            log.debug("Token length: {}", token.length());
            
            Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
                
            log.debug("Successfully parsed token claims");
            return claims;
        } catch (Exception e) {
            log.error("Error extracting all claims from token: {}", e.getMessage());
            log.error("Token that failed: {}", token.substring(0, Math.min(50, token.length())) + "...");
            throw e;
        }
    }

    private Key getSignInKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Error creating signing key: {}", e.getMessage());
            throw e;
        }
    }
}
