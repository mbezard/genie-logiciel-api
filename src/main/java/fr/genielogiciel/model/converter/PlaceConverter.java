package fr.genielogiciel.model.converter;

import fr.genielogiciel.model.dto.PlaceWithScoreDto;
import fr.genielogiciel.model.entity.Place;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PlaceConverter {

    public PlaceWithScoreDto toPlaceWithScoreDto(Place place, Integer score) {
        PlaceWithScoreDto placeWithScoreDto = new PlaceWithScoreDto();
        placeWithScoreDto.setId(place.getId());
        placeWithScoreDto.setTags(place.getTags());
        placeWithScoreDto.setAddress(place.getAddress());
        placeWithScoreDto.setTitle(place.getTitle());
        placeWithScoreDto.setLatitude(place.getLatitude());
        placeWithScoreDto.setLongitude(place.getLongitude());

        placeWithScoreDto.setScore(score);

        return placeWithScoreDto;
    }

    public List<PlaceWithScoreDto> toPlaceWithScoreDto(List<Map.Entry<Place, Integer>> places) {
        return places.stream()
                .map(placeIntegerEntry -> toPlaceWithScoreDto(placeIntegerEntry.getKey(), placeIntegerEntry.getValue()))
                .collect(Collectors.toList());
    }
}
