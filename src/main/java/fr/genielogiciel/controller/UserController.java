package fr.genielogiciel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.genielogiciel.model.converter.PlaceConverter;
import fr.genielogiciel.model.converter.UserConverter;
import fr.genielogiciel.model.dto.PlaceDto;
import fr.genielogiciel.model.dto.PlaceWithScoreDto;
import fr.genielogiciel.model.dto.UserBasicDto;
import fr.genielogiciel.model.entity.Place;
import fr.genielogiciel.model.entity.Tag;
import fr.genielogiciel.model.entity.User;
import fr.genielogiciel.model.repository.PlaceRepository;
import fr.genielogiciel.model.repository.TagRepository;
import fr.genielogiciel.model.repository.UserRepository;
import fr.genielogiciel.utils.GeneralService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final GeneralService generalService;
    private final UserConverter userConverter;
    private final PlaceConverter placeConverter;
    private final PasswordEncoder passwordEncoder;
    private final PlaceRepository placeRepository;


    @Autowired
    public UserController(UserRepository userRepository, TagRepository tagRepository, PlaceRepository placeRepository, GeneralService generalService, UserConverter userConverter, PlaceConverter placeConverter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.placeRepository = placeRepository;
        this.generalService = generalService;
        this.userConverter = userConverter;
        this.placeConverter = placeConverter;
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

    @PutMapping(path = "/add-tag/{tagId}")
    private ResponseEntity<String> addTag(@PathVariable Integer tagId) {
        User user = generalService.getUserFromContext();
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new EntityNotFoundException("No tag with this id"));

        if(!user.getTags().contains(tag)) user.getTags().add(tag);
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/remove-tag/{tagId}")
    private ResponseEntity<String> removeTag(@PathVariable Integer tagId) {
        User user = generalService.getUserFromContext();
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new EntityNotFoundException("No tag with this id"));

        user.getTags().remove(tag);
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/add-visited-place/{placeId}")
    private ResponseEntity<String> addVisitedPlace(@PathVariable Integer placeId) {
        User user = generalService.getUserFromContext();
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new EntityNotFoundException("No place with this id"));

        if (user.getPlacesVisited().contains(place)) {
            user.getPlacesVisited().remove(place);
        }
        user.getPlacesVisited().add(0,place); //inserted in the first position to order the whole in chronological order

        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-visited-places")
    private List<PlaceDto> getVisitedPlaces (){
        User user = generalService.getUserFromContext();
        List<Place> visitedPlaces = user.getPlacesVisited();
        return placeConverter.toPlaceDto(visitedPlaces);
    }
}
