package fr.genielogiciel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.genielogiciel.model.converter.PlaceConverter;
import fr.genielogiciel.model.dto.PlaceWithScoreDto;
import fr.genielogiciel.model.entity.Place;
import fr.genielogiciel.model.entity.Tag;
import fr.genielogiciel.model.repository.PlaceRepository;
import fr.genielogiciel.utils.GeneralService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PlaceController(GeneralService generalService, PlaceRepository placeRepository, PlaceConverter placeConverter) {
        this.generalService = generalService;
        this.placeRepository = placeRepository;
        this.placeConverter = placeConverter;
    }

    @PostMapping
    private List<PlaceWithScoreDto> getPlacesFromTags(
            @RequestParam String tagsAsString,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude
    ) throws JSONException, JsonProcessingException {
        List<Tag> tags = generalService.getObjectListFromJsonString(tagsAsString, Tag.class);

        List<Place> possiblePlaces = placeRepository.findDistinctByTagsIn(tags);
//        System.out.println("number of possible : " + possiblePlaces.size());

        List<Map.Entry<Place, Integer>> placesAndScores = new ArrayList<>();

        for (int i = 0; i < Math.min(31, possiblePlaces.size()); i++) {
            placesAndScores.add(new AbstractMap.SimpleEntry<>(possiblePlaces.get(i), 0));
        }

        for (Map.Entry<Place, Integer> placesAndScore : placesAndScores) {
            //Increment score if match with multiple tags
            for (Tag tag : tags) {
                if (placesAndScore.getKey().getTags().contains(tag)) {
                    placesAndScore.setValue(placesAndScore.getValue() + 10);
                }
            }

            //Multiply score if not too far
            if (longitude != null && latitude != null) {
                double distance = Math.sqrt(
                        Math.pow(latitude - placesAndScore.getKey().getLatitude(), 2) +
                                Math.pow(longitude - placesAndScore.getKey().getLongitude(), 2)
                );
                distance = Math.pow(distance, 1.5) * 10; //make distance more important (optional)
                double multiplier = Math.max(distance * ((0.1 - 10) / (0.1 - 0)) + 10, 0.1);

//                System.out.println("id : " + placesAndScore.getKey().getTitle() +
//                        " distance : " + distance +
//                        " multiplier : " + multiplier +
//                        " score : " + (int) (placesAndScore.getValue() * multiplier));

                placesAndScore.setValue((int) (placesAndScore.getValue() * multiplier));
            }
        }

        placesAndScores.sort((o1, o2) -> -o1.getValue().compareTo(o2.getValue()));
//        System.out.println(placesAndScores);
//        System.out.println("number of final : " + placesAndScores.size());
        return placeConverter.toPlaceWithScoreDto(placesAndScores);
    }
}
