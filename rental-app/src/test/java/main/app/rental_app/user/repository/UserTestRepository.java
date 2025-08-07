package main.app.rental_app.user.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import main.app.rental_app.user.model.User;
import main.app.rental_app.user.model.enums.Role;

@DataJpaTest
@ActiveProfiles("test")
public class UserTestRepository {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        // Given
        User user = User.builder()
            .username("test")
            .password("password")
            .email("test@test.com")
            .role(Role.user)
            .build();

        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
        assertEquals(user.getRole(), foundUser.get().getRole());
    }

    @Test
    public void testFindByUsername_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testSaveUser() {
        // Given
        User user = User.builder()
            .username("newuser")
            .password("password")
            .email("newuser@test.com")
            .role(Role.admin)
            .build();

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getRole(), savedUser.getRole());
    }
}
