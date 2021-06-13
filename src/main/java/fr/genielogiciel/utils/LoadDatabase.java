package fr.genielogiciel.utils;

import fr.genielogiciel.model.entity.Place;
import fr.genielogiciel.model.entity.Tag;
import fr.genielogiciel.model.entity.User;
import fr.genielogiciel.model.repository.PlaceRepository;
import fr.genielogiciel.model.repository.TagRepository;
import fr.genielogiciel.model.repository.UserRepository;
import fr.genielogiciel.security.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, TagRepository tagRepository, PlaceRepository placeRepository, PasswordEncoder encoder) {
        User basic = new User();
        basic.setName("Basic User");
        basic.setMail("user@user.com");
        basic.setPassword(encoder.encode("password"));

        User admin = new User();
        admin.setName("Admin User");
        admin.setMail("admin@admin.com");
        admin.setRole(Role.ADMIN);
        admin.setPassword(encoder.encode("matthieu"));


        List<Tag> tags = new ArrayList<>();
        List<String> tagTitles = List.of(
                "StreetArt", "Musées", "Expositions temporaires", "Statues", "Art Contemporain",
                "Art moderne", "Ruines", "Fresques murales", "Pixel Art");
        List<Place> places = new ArrayList<>();
        if (!tagRepository.findAll().iterator().hasNext()) {
            for (String title : tagTitles) {
                tags.add(new Tag(title));
            }

            for(int i=0; i < (Math.random()*30+20); i++) {
                places.add(
                        new Place(
                                "Fake place n°"+i,
                                "Fake address",
                                tags.stream().filter((tag) -> (Math.random() > 0.7)).collect(Collectors.toList()),
                                "Fake description",
                                48.85 + Math.random() - 0.5,
                                2.35 + Math.random() - 0.5,
                                basic
                        )
                );
            }
        }

        return args -> {
            if (repository.findByMail("user@user.com").isEmpty())
                log.info("Preloading basic user " + repository.save(basic));
            if (repository.findByMail("admin@admin.com").isEmpty())
                log.info("Preloading admin user " + repository.save(admin));
            log.info("Preloading tags " + tagRepository.saveAll(tags));
            log.info("Preloading places " + placeRepository.saveAll(places));
        };
    }
}
