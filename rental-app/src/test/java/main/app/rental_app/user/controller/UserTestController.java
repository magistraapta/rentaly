package main.app.rental_app.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.user.service.UserService;
import main.app.rental_app.user.model.dto.UserDto;
import main.app.rental_app.user.model.enums.Role;

public class UserTestController {

    private MockMvc mockMvc;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserService() {
            @Override
            public UserDto getUserById(Long id) throws ResourceNotFoundException {
                return UserDto.builder()
                    .id(id)
                    .username("user" + id)
                    .email("user" + id + "@test.com")
                    .role(Role.user)
                    .build();
            }

            @Override
            public java.util.Optional<UserDto> getUserByUsername(String username) throws ResourceNotFoundException {
                return java.util.Optional.of(UserDto.builder()
                    .id(1L)
                    .username(username)
                    .email(username + "@test.com")
                    .role(Role.user)
                    .build());
            }

            @Override
            public UserDto createUser(UserDto userDto) {
                return userDto;
            }
        };

        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testFindByUsername() throws Exception {
        // When & Then
        mockMvc.perform(get("/v1/users/username/user1"))
            .andExpect(status().isOk());
    }
}
