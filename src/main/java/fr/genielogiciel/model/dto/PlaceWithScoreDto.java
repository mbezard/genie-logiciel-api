package fr.genielogiciel.model.dto;

import fr.genielogiciel.model.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class PlaceWithScoreDto {
        private Integer id;

//        private UserDto author;

        private List<Tag> tags;
        private String title;
        private String address;

        private double latitude;
        private double longitude;

        private String description;
        private String url;

        private Integer score;

}
