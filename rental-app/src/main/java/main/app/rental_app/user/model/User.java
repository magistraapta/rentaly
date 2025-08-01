package main.app.rental_app.user.model;

import lombok.Builder;
import lombok.Data;
import main.app.rental_app.user.model.enums.Role;

@Data
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
}
