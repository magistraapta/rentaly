package main.app.rental_app.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import main.app.rental_app.auth.model.dto.request.LoginRequest;
import main.app.rental_app.auth.model.dto.request.RefreshTokenDto;
import main.app.rental_app.auth.model.dto.request.RegisterRequest;
import main.app.rental_app.auth.service.AuthService;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization")
public class AuthController {
    private final AuthService authService;

    @Operation(
        summary = "Register a new user",
        description = "Create a new user account with username, password, and email"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User registered successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid input data or user already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
        @Parameter(description = "User registration details", required = true)
        @Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(
        summary = "User login",
        description = "Authenticate user credentials and return access token and refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful - Returns access token and refresh token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid credentials or input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid username or password",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @Parameter(description = "User login credentials", required = true)
        @Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(
        summary = "Refresh access token",
        description = "Generate a new access token using a valid refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token refreshed successfully - Returns new access token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid refresh token or input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid or expired refresh token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
        @Parameter(description = "Refresh token request", required = true)
        @Valid @RequestBody RefreshTokenDto request) {
        return authService.refreshToken(request);
    }

    @Operation(
        summary = "User logout",
        description = "Logout user and invalidate refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Logout successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid or missing token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return authService.logout(request);
    }

    @Operation(
        summary = "Get current user",
        description = "Retrieve information about the currently authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User information retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid or missing token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        return authService.getCurrentUser(request);
    }
}
