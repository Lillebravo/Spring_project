package com.jerry.school_project.controller;

import com.jerry.school_project.dto.BookWithDetailsDTO;
import com.jerry.school_project.entity.Book;
import com.jerry.school_project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Get all books
     * Example: GET localhost:8080/api/books
     */
    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        try {
            List<BookWithDetailsDTO> books = bookService.getAllBooks();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "total", books.size(),
                    "books", books
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Server error occurred while retrieving books"
            ));
        }
    }

    /**
     * Search books by title or author name
     * Example: GET localhost:8080/api/books/search/harry potter
     */
    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchBooks(@PathVariable String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Search query cannot be empty"
                ));
            }

            List<BookWithDetailsDTO> books = bookService.searchBooks(query);

            if (books.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "status", "not found",
                        "message", "No books found with title or author name containing '" + query + "'"
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "total", books.size(),
                    "books", books
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "An unexpected error occurred: " + e.getMessage()
            ));
        }
    }

    /**
     * Add a new book
     * Example: POST localhost:8080/api/books
     */
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            if (book == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Book data is required"
                ));
            }

            Optional<BookWithDetailsDTO> result = bookService.addBook(book);

            if (result.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "status", "success",
                        "message", "Book created successfully",
                        "book", result.get()
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Failed to create book. Please check your input data."
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
                    "message", "An unexpected error occurred while creating the book: " + e.getMessage()
            ));
        }
    }
}