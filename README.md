# GENIE-LOGICIEL-API - WANDER

[Spring Boot](http://projects.spring.io/spring-boot/) app.

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/fr/java/technologies/javase-jdk11-downloads.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `de.codecentric.springbootsample.Application` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

##Les variables d'environnement
###Sur Intellij :

1-Lancer une fois

2-Editer la configuration

3-Dans Configuration > Environment > Environment variables :\
-Ajouter 3 variables : DB_URL, DB_USER, DB_PASSWORD (DB_URL doit etre de la forme jdbc:mysql://localhost:3306/nomdelabase) \

4-C'est bon
