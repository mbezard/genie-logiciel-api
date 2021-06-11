package fr.genielogiciel.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
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

}
