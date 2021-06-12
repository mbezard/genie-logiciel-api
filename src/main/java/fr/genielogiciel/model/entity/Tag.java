package fr.genielogiciel.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    public Tag(String title) {
        this.title = title;
    }

}
