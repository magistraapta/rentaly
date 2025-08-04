package main.app.rental_app.auth.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.auth.jwt.JwtUtil;
import main.app.rental_app.auth.jwt.JwtService;
import main.app.rental_app.auth.mapper.AuthMapper;
import main.app.rental_app.auth.model.dto.request.LoginRequest;
import main.app.rental_app.auth.model.dto.request.RefreshTokenDto;
import main.app.rental_app.auth.model.dto.request.RegisterRequest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import main.app.rental_app.user.model.User;
import main.app.rental_app.user.model.enums.Role;
import main.app.rental_app.user.repository.UserRepository;
import main.app.rental_app.auth.model.RefreshToken;
import main.app.rental_app.auth.repository.RefreshTokenRepository;
import main.app.rental_app.exc.BadRequestException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {
    
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        User newUser = User.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))  // Only encode once
                    .role(Role.user)
                    .build();

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(authMapper.userToRegisterResponseDto(newUser));
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        User user = authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        
        // Revoke any existing refresh tokens for this user
        revokeAllTokensForUser(user);
        
        String accessToken = jwtService.generateToken(user);
        String refreshToken = createAndSaveRefreshToken(user);

        var response = authMapper.userToLoginResponseDto(user, accessToken, refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                            .body(response);
    }

    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenDto dto) {
        RefreshToken oldToken = refreshTokenRepository.findByToken(dto.getRefreshToken())
            .orElseThrow(() -> new BadRequestException("Refresh token not found"));
    
        if (oldToken.isRevoked() || oldToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Refresh token expired");
        }

        User user = oldToken.getUser();
        String token = jwtService.generateToken(user);
        String refreshToken = createAndSaveRefreshToken(user);

        var response = authMapper.userToLoginResponseDto(user, token, refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                            .body(response);
    }

    @Override
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);

        if (token == null) {
            throw new BadRequestException("Token not found");
        }

        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("User not found"));
        
        log.info("Logging out user: {}", username);
        revokeAllTokensForUser(user);
        log.info("Revoked all refresh tokens for user: {}", username);

        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK)
                            .body("Logged out successfully");
    }


    private User authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        return (User) authentication.getPrincipal();    
    }

    private String createAndSaveRefreshToken(User user) {
        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken refreshTokenEntity = authMapper.userToRefreshToken(user, refreshToken, Instant.now().plusMillis(refreshTokenExpiration));

        try {
            refreshTokenRepository.save(refreshTokenEntity);
        } catch (Exception e) {
            // Log the error and throw a more specific exception
            throw new BadRequestException("Failed to save refresh token: " + e.getMessage());
        }

        return refreshToken;
    }

    private void revokeAllTokensForUser(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserAndIsRevokedFalse(user);
        log.info("Found {} active refresh tokens for user: {}", tokens.size(), user.getUsername());
        
        tokens.forEach(t -> t.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
        
        log.info("Successfully revoked {} refresh tokens for user: {}", tokens.size(), user.getUsername());
    }

    
}
