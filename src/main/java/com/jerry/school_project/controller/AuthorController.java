package com.jerry.school_project.controller;

import com.jerry.school_project.entity.Author;
import com.jerry.school_project.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@CrossOrigin(origins = "*")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * Get all authors
     * Example: GET localhost:8080/api/authors
     */
    @GetMapping
    public ResponseEntity<?> getAllAuthors() {
        try {
            List<Author> authors = authorService.getAllAuthors();
            return ResponseEntity.status(200).body(authors);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while retrieving authors");
        }
    }

    /**
     * Find authors by last name
     * Example: GET localhost:8080/api/authors/name/smith
     */
    @GetMapping("/name/{lastName}")
    public ResponseEntity<?> findAuthorsByLastName(@PathVariable String lastName) {
        try {
            List<Author> authors = authorService.findAuthorsByLastName(lastName);

            if (authors.isEmpty()) {
                return ResponseEntity.status(404).body("No authors found with last name: " + lastName);
            }

            return ResponseEntity.status(200).body(authors);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while searching for authors");
        }
    }

    /**
     * Add a new author
     * Example: POST localhost:8080/api/authors
     */
    @PostMapping
    public ResponseEntity<?> addAuthor(@RequestBody Author author) {
        try {
            Author createdAuthor = authorService.addAuthor(author);
            return ResponseEntity.status(201).body(createdAuthor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while creating the author");
        }
    }
}