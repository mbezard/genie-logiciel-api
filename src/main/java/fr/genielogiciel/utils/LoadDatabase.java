package fr.genielogiciel.utils;

import fr.genielogiciel.model.entity.User;
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


@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, PasswordEncoder encoder) {
        User basic = new User();
        basic.setName("Basic User");
        basic.setMail("user@user.com");
        basic.setPassword(encoder.encode("password"));

        User admin = new User();
        admin.setName("Admin User");
        admin.setMail("admin@admin.com");
        admin.setRole(Role.ADMIN);
        admin.setPassword(encoder.encode("matthieu"));



        return args -> {
            if (repository.findByMail("user@user.com").isEmpty())
                log.info("Preloading basic user " + repository.save(basic));
            if (repository.findByMail("admin@admin.com").isEmpty())
                log.info("Preloading admin user " + repository.save(admin));

        };
    }
}
