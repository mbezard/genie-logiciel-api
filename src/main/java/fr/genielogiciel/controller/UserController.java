package fr.genielogiciel.controller;

import fr.genielogiciel.model.converter.UserConverter;
import fr.genielogiciel.model.dto.UserBasicDto;
import fr.genielogiciel.model.entity.User;
import fr.genielogiciel.model.repository.UserRepository;
import fr.genielogiciel.utils.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping(path = "/modify-profile")
    String modifyUserMail(@RequestParam String name, @RequestParam String mail, @RequestParam String password) {
        User user = generalService.getUserFromContext();
        user.setName(name);
        user.setMail(mail);
        if (!password.equals("undefined") && password.length() > 1){
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepository.save(user);
        return "Changed";
    }

    @PostMapping(path = "/signin")
    private ResponseEntity<String> signin(
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam String mail
    ) {
        if(userRepository.findByMail(mail).isPresent()) return new ResponseEntity<>("A user with this mail already exists.", HttpStatus.CONFLICT);

        User user = new User();
        user.setMail(mail);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);

        userRepository.save(user);

        return new ResponseEntity<>("User added", HttpStatus.OK);
    }

}
