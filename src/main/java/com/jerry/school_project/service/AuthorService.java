package com.jerry.school_project.service;

import com.jerry.school_project.entity.Author;
import com.jerry.school_project.repository.AuthorRepository;
import com.jerry.school_project.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        try {
            if (lastName == null || lastName.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return authorRepository.findByLastName(lastName.trim());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Add a new author
    public Optional<Author> addAuthor(Author author) {
        try {
            String firstName = author.getFirstName() != null ? author.getFirstName().trim() : null;
            String lastName = author.getLastName() != null ? author.getLastName().trim() : null;
            String nationality = author.getNationality() != null ? author.getNationality().trim() : null;

            // Validate all required fields using ValidationUtils
            validation.validateString(firstName, "First name");
            validation.validateString(lastName, "Last name");
            validation.validateString(nationality, "Nationality");
            validation.validateBirthYear(author.getBirthYear(), true);

            // Check if author already exists with same first name and last name
            if (authorRepository.existsByFirstNameAndLastName(firstName, lastName)) {
                throw new IllegalArgumentException("An author with this first name and last name already exists");
            }

            Author savedAuthor = authorRepository.save(author);
            return Optional.of(savedAuthor);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}