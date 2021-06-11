package fr.genielogiciel.model.dto;

import fr.genielogiciel.model.entity.Tag;
import fr.genielogiciel.security.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserBasicDto {
    private String name;
    private String mail;
    private Role role = Role.USER;
    private List<Tag> tags;

}
