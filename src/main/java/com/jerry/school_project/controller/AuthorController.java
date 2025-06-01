package com.jerry.school_project.controller;

import com.jerry.school_project.entity.Author;
import com.jerry.school_project.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "total", authors.size(),
                    "authors", authors
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Server error occurred while retrieving authors"
            ));
        }
    }

    /**
     * Find authors by last name
     * Example: GET localhost:8080/api/authors/name/smith
     */
    @GetMapping("/name/{lastName}")
    public ResponseEntity<?> findAuthorsByLastName(@PathVariable String lastName) {
        try {
            if (lastName == null || lastName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Last name cannot be empty"
                ));
            }

            List<Author> authors = authorService.findAuthorsByLastName(lastName);

            if (authors.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "status", "not found",
                        "message", "No authors found with last name containing '" + lastName + "'"
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "total", authors.size(),
                    "authors", authors
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "An unexpected error occurred: " + e.getMessage()
            ));
        }
    }

    /**
     * Add a new author
     * Example: POST localhost:8080/api/authors
     */
    @PostMapping
    public ResponseEntity<?> addAuthor(@RequestBody Author author) {
        try {
            if (author == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Author data is required"
                ));
            }

            Optional<Author> result = authorService.addAuthor(author);

            if (result.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "status", "success",
                        "message", "Author created successfully",
                        "author", result.get()
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Failed to create author. Please check your input data."
                ));
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "An unexpected error occurred while creating the author: " + e.getMessage()
            ));
        }
    }
}