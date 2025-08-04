package main.app.rental_app.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.user.model.dto.UserDto;
import main.app.rental_app.user.model.mapper.UserMapper;
import main.app.rental_app.user.repository.UserRepository;
import main.app.rental_app.user.model.User;

/**
 * UserServiceImpl implements UserService interface
 * Handle the business logic of the user module
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(Long id) throws ResourceNotFoundException {
        try {
            log.info("Getting user by id");
            User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return userMapper.toDto(user);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User not found"); 
        }
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) throws ResourceNotFoundException {
        try {
            log.info("Getting user by username");
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return Optional.of(userMapper.toDto(user));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User not found"); 
        }
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        try {
            log.info("Creating user");
            User user = userMapper.toEntity(userDto);
            userRepository.save(user);
            return userMapper.toDto(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

}
