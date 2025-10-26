package main.app.rental_app.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.user.model.dto.UserDto;

@Service
public interface UserService {
    UserDto getUserById(Long id) throws ResourceNotFoundException;
    Optional<UserDto> getUserByUsername(String username) throws ResourceNotFoundException;
    UserDto createUser(UserDto userDto);
    Boolean existsByEmail(String email);
}
