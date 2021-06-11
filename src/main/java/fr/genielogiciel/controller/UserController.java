package fr.genielogiciel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.genielogiciel.model.converter.UserConverter;
import fr.genielogiciel.model.dto.UserBasicDto;
import fr.genielogiciel.model.entity.Tag;
import fr.genielogiciel.model.entity.User;
import fr.genielogiciel.model.repository.UserRepository;
import fr.genielogiciel.utils.GeneralService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserRepository userRepository;
    private final GeneralService generalService;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, GeneralService generalService, UserConverter userConverter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.generalService = generalService;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/get-user-info")
    private UserBasicDto getUserInfoUsingToken() {
        return userConverter.toBasicDto(generalService.getUserFromContext());
    }


    @PostMapping
    private ResponseEntity<String> modifyUser(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String mail,
                                              @RequestParam(required = false) String password,
                                              @RequestParam(required = false) String tagsAsString
    ) throws JSONException, JsonProcessingException {
        User user = generalService.getUserFromContext();
        List<Tag> tags = null;
        if (tagsAsString != null && tagsAsString.length() > 0) {
            tags = generalService.getObjectListFromJsonString(tagsAsString, Tag.class);
        }

        if (mail != null && mail.length() > 0) {
            if (!user.getMail().equals(mail) && userRepository.findByMail(mail).isPresent()) {
                return new ResponseEntity<>("Mail already used", HttpStatus.BAD_REQUEST);
            } else {
                user.setMail(mail);
            }
        }
        if (name != null && name.length() > 0) user.setName(name);
        if (password != null && password.length() > 0) user.setPassword(passwordEncoder.encode(password));
        if (tags != null) user.setTags(tags);

        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/signin")
    private ResponseEntity<String> signin(
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam String mail
    ) {
        if (userRepository.findByMail(mail).isPresent())
            return new ResponseEntity<>("A user with this mail already exists.", HttpStatus.CONFLICT);

        User user = new User();
        user.setMail(mail);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);

        userRepository.save(user);

        return new ResponseEntity<>("User added", HttpStatus.OK);
    }

}
