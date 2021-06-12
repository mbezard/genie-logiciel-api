package fr.genielogiciel.model.dto;

import fr.genielogiciel.model.entity.Tag;
import fr.genielogiciel.model.entity.User;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Data
public class PlaceWithScoreDto {
        private Integer id;

//        private UserDto author;

        private List<Tag> tags;
        private String title;
        private String address;

        private Integer score;

}
