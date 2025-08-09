package main.app.rental_app.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.user.model.dto.UserDto;
import main.app.rental_app.user.service.UserService;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoint to manage user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user id")
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            return userService.getUserById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @Operation(summary = "Get user by username")
    @GetMapping("/{username}")
    public UserDto getUserByUsername(@PathVariable String username) throws ResourceNotFoundException {
        try {
            return userService.getUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User not found");
        }
    }
}
