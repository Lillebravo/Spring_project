package com.jerry.school_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SchoolProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolProjectApplication.class, args);
        System.out.println("Welcome to School Project");

    }

    @RestController
    class HiController {
        @GetMapping("/hi")
        public String say() {
            return "Hi from Spring Boot!";
        }
    }

}
