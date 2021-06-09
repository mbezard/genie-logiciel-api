package fr.genielogiciel.model.dto;

import fr.genielogiciel.security.Role;
import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String mail;
    private Role role = Role.USER;
}
