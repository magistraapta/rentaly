package main.app.rental_app.auth.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.Instant;

import main.app.rental_app.auth.model.dto.response.LoginResponseDto;
import main.app.rental_app.auth.model.dto.response.RegisterResponseDto;
import main.app.rental_app.auth.model.dto.response.UserResponseDto;
import main.app.rental_app.user.model.User;
import main.app.rental_app.auth.model.RefreshToken;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    UserResponseDto userToUserResponseDto(User user);
    RegisterResponseDto userToRegisterResponseDto(User user);   

    @Mapping(target = "userResponseDto", source = "user")
    LoginResponseDto userToLoginResponseDto(User user, String accessToken, String refreshToken);

    @Named("userToRefreshToken")
    default RefreshToken userToRefreshToken(User user, String token, Instant expiryDate) {
        return RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(expiryDate)
                .isRevoked(false)
                .version(0L)
                .build();
    }
}
