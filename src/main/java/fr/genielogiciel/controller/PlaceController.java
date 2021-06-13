package fr.genielogiciel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.genielogiciel.model.converter.PlaceConverter;
import fr.genielogiciel.model.dto.PlaceWithScoreDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@RestController
@RequestMapping(path = "/place")
public class PlaceController {

    private final GeneralService generalService;
    private final PlaceRepository placeRepository;
    private final PlaceConverter placeConverter;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Autowired
    public PlaceController(GeneralService generalService, PlaceRepository placeRepository, PlaceConverter placeConverter, TagRepository tagRepository, UserRepository userRepository) {
        this.generalService = generalService;
        this.placeRepository = placeRepository;
        this.placeConverter = placeConverter;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    private List<PlaceWithScoreDto> getPlacesFromTags(@RequestParam String tagsAsString) throws JSONException, JsonProcessingException {
        List<Tag> tags = generalService.getObjectListFromJsonString(tagsAsString, Tag.class);

        List<Place> possiblePlaces = placeRepository.findAllByTagsIn(tags);
        System.out.println(possiblePlaces);

        List<Map.Entry<Place, Integer>> placesAndScores = new ArrayList<>();

        possiblePlaces.forEach(place -> placesAndScores.add(new AbstractMap.SimpleEntry<>(place, 0)));

        for (Map.Entry<Place, Integer> placesAndScore : placesAndScores) {
            for (Tag tag : tags) {
                if (placesAndScore.getKey().getTags().contains(tag)) {
                    placesAndScore.setValue(placesAndScore.getValue() + 1);
                }

            }
        }

        placesAndScores.sort((o1, o2) -> -o1.getValue().compareTo(o2.getValue()));
        System.out.println(placesAndScores);

        return placeConverter.toPlaceWithScoreDto(placesAndScores);
    }

    @PostMapping(path = "/addPlace")
    private ResponseEntity<String> addPlace(
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String tagsAsString,
            @RequestParam String description,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam String authorMail
    ) throws JSONException, JsonProcessingException {
        List<Tag> tags = generalService.getObjectListFromJsonString(tagsAsString, Tag.class);
        User author = userRepository.findByMail(authorMail).orElseThrow(() -> new EntityNotFoundException("No user with this id"));

        Place place = new Place(name, address, tags, description, latitude, longitude, author );
        placeRepository.save(place);

        return new ResponseEntity<>("Place added", HttpStatus.OK);
    }
}
