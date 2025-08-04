package main.app.rental_app.user.model.mapper;

import org.mapstruct.Mapper;

import main.app.rental_app.user.model.User;
import main.app.rental_app.user.model.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
