package fr.genielogiciel.model.converter;

import fr.genielogiciel.model.dto.UserBasicDto;
import fr.genielogiciel.model.dto.UserDto;
import fr.genielogiciel.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    ModelMapper modelMapper = new ModelMapper();

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        modelMapper.map(user, userDto);

        return userDto;
    }

    public UserBasicDto toBasicDto(User user) {
        UserBasicDto userDto = new UserBasicDto();
        userDto.setMail(user.getMail());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole());
        userDto.setTags(user.getTags());
        return userDto;
    }

}
