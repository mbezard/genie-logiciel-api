package fr.genielogiciel.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Place {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private User author;

    @ManyToMany
    private List<Tag> tags;

    private String title;
    private String address;

    private String description;

    private double latitude;
    private double longitude;

    public Place(String title, String address, List<Tag> tags,String description, double latitude, double longitude, User author) {
        this.title = title;
        this.tags = tags;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.author = author;
    }


}
