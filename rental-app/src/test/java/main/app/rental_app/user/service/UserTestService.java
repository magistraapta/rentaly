package main.app.rental_app.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    public void testUserFindById_NotFound() {
        // Arrange
        Long id = 999L;

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(id);
        });
    }

    @Test
    public void testUserFindById_Found() {
        // Arrange
        Long id = 1L;
        User user = User.builder()
            .id(id)
            .username("test")
            .password("password")
            .email("test@test.com")
            .role(Role.user)
            .build();

        UserDto userDto = UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
            
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        UserDto foundUser = userService.getUserById(id);

        // Assert
        assertEquals(userDto, foundUser);
        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    public void testUserFindByUsername_Found() {
        // Arrange
        User user = User.builder()
            .id(1L)
            .username("test")
            .password("password")
            .email("test@test.com")
            .role(Role.user)
            .build();

        UserDto userDto = UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole())
            .build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        Optional<UserDto> foundUser = userService.getUserByUsername(user.getUsername());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(userDto, foundUser.get());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    public void testCreateUser() {
        // Arrange
        UserDto inputUserDto = UserDto.builder()
            .username("test")
            .password("password")
            .email("test@test.com")
            .role(Role.user)
            .build();

        User userEntity = User.builder()
            .id(1L)
            .username("test")
            .password("password")
            .email("test@test.com")
            .role(Role.user)
            .build();

        UserDto expectedUserDto = UserDto.builder()
            .id(1L)
            .username("test")
            .password("password")
            .email("test@test.com")
            .role(Role.user)
            .build();

        when(userMapper.toEntity(inputUserDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(expectedUserDto);

        // Act
        UserDto createdUser = userService.createUser(inputUserDto);

        // Assert
        assertEquals(expectedUserDto, createdUser);
        verify(userMapper, times(1)).toEntity(inputUserDto);
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toDto(userEntity);
    }
}
