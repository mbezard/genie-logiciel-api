package fr.genielogiciel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.genielogiciel.model.converter.PlaceConverter;
import fr.genielogiciel.model.dto.PlaceWithScoreDto;
import fr.genielogiciel.model.entity.Place;
import fr.genielogiciel.model.entity.Tag;
import fr.genielogiciel.model.repository.PlaceRepository;
import fr.genielogiciel.model.repository.TagRepository;
import fr.genielogiciel.utils.GeneralService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(path = "/place")
public class PlaceController {

    private final GeneralService generalService;
    private final PlaceRepository placeRepository;
    private final PlaceConverter placeConverter;
    private final TagRepository tagRepository;

    @Autowired
    public PlaceController(GeneralService generalService, PlaceRepository placeRepository, PlaceConverter placeConverter, TagRepository tagRepository) {
        this.generalService = generalService;
        this.placeRepository = placeRepository;
        this.placeConverter = placeConverter;
        this.tagRepository = tagRepository;
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
            @RequestParam String nom,
            @RequestParam String adresse,
            @RequestParam String inputTags
    ){


        List<Tag> list = new ArrayList<>();

        if (tagRepository.findByTitle(inputTags).isEmpty()){
            list.add(tagRepository.findByTitle(inputTags).get());
        }
        else{
            list.add(new Tag(inputTags));
        }

        Place place = new Place(nom, list, 0.0, 0.0 );
        placeRepository.save(place);
        return new ResponseEntity<>("Place added", HttpStatus.OK);
    }
}
