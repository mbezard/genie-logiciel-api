package fr.genielogiciel.model.repository;

import fr.genielogiciel.model.entity.Tag;
import fr.genielogiciel.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {
    Optional<Tag> findByTitle(String Title);

}
