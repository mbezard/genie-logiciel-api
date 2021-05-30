package fr.genielogiciel.controller;

import fr.genielogiciel.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {
    @Autowired
    UserRepository userRepository;
    @GetMapping(path = "/")
    private String hello() {
        System.out.println("Hello");
        return "Hello";
    }

    @PostMapping(path = "/")
    private String helloPost() {
        System.out.println("Hello POST");
        return "Hello (POST)";
    }

    @GetMapping(path = "/admin")
    private String admin() {
        return "Welcome admin";
    }

    @GetMapping(path = "/auth")
    private String connected() {
        return "You are connected";
    }

    @PostMapping(path = "/auth")
    private String connectedPost() {
        return "You are connected (POST)";
    }
}
