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

    public Place(String title, List<Tag> tags) {
        this.title = title;
        this.tags = tags;
    }

}
