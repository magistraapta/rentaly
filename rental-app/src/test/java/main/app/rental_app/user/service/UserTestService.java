package main.app.rental_app.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.user.model.User;
import main.app.rental_app.user.model.dto.UserDto;
import main.app.rental_app.user.model.enums.Role;
import main.app.rental_app.user.model.mapper.UserMapper;
import main.app.rental_app.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserTestService {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testFindByUsername_Success() throws ResourceNotFoundException {
        // Given
        User user = User.builder()
            .username("test")
            .password("password")
            .email("test@test.com")
            .role(Role.user)
            .build();

        UserDto userDto = UserDto.builder()
            .username("test")
            .password("password")
            .email("test@test.com")
            .role(Role.user)
            .build();

        // When
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toDto(user)).thenReturn(userDto);

        Optional<UserDto> foundUser = userService.getUserByUsername(user.getUsername());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
        assertEquals(user.getRole(), foundUser.get().getRole());
    }

    @Test
    public void testFindByUsername_NotFound() throws ResourceNotFoundException {
        // Given
        String username = "nonexistent";

        // When
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByUsername(username);
        });
    }
}
