package fr.genielogiciel.model.repository;

import fr.genielogiciel.model.entity.Place;
import fr.genielogiciel.model.entity.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Integer> {
    List<Place> findAllByTagsContaining(Tag tag);
    List<Place> findAllByTagsIn(List<Tag> tags);
}
