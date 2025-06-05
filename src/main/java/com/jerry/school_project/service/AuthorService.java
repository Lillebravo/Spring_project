package com.jerry.school_project.service;

import com.jerry.school_project.entity.Author;
import com.jerry.school_project.repository.AuthorRepository;
import com.jerry.school_project.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final Validation validation;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, Validation validation) {
        this.authorRepository = authorRepository;
        this.validation = validation;
    }

    // Get all authors
    public List<Author> getAllAuthors() {
        try {
            return authorRepository.findAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Find authors by last name
    public List<Author> findAuthorsByLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }

        try {
            return authorRepository.findByLastNameContainingIgnoreCase(lastName.trim());
        } catch (Exception e) {
            throw new RuntimeException("Database error occurred while searching for authors", e);
        }
    }

    // Add a new author
    public Author addAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Author data is required");
        }

        try {
            String firstName = author.getFirstName() != null ? author.getFirstName().trim() : null;
            String lastName = author.getLastName() != null ? author.getLastName().trim() : null;
            String nationality = author.getNationality() != null ? author.getNationality().trim() : null;

            // Validation
            validation.validateString(firstName, "First name");
            validation.validateString(lastName, "Last name");
            validation.validateString(nationality, "Nationality");
            validation.validateBirthYear(author.getBirthYear(), true);

            // Check if author already exists with same first name and last name
            if (authorRepository.existsByFirstNameAndLastName(firstName, lastName)) {
                throw new IllegalArgumentException("An author with this first name and last name already exists");
            }

            // Set the trimmed values back to the author object
            author.setFirstName(firstName);
            author.setLastName(lastName);
            author.setNationality(nationality);

            return authorRepository.save(author);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create author due to system error", e);
        }
    }
}