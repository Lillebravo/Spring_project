package com.jerry.school_project.controller;

import com.jerry.school_project.dto.BookWithDetailsDTO;
import com.jerry.school_project.entity.Book;
import com.jerry.school_project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return ResponseEntity.status(200).body(books);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while retrieving books");
        }
    }

    /**
     * Search books by title or author name
     * Example: GET localhost:8080/api/books/search/harry potter
     */
    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchBooks(@PathVariable String query) {
        try {
            List<BookWithDetailsDTO> books = bookService.searchBooks(query);

            if (books.isEmpty()) {
                return ResponseEntity.status(404).body("No books found with title or author name containing '" + query + "'");
            }

            return ResponseEntity.status(200).body(books);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while searching books");
        }
    }

    /**
     * Add a new book
     * Example: POST localhost:8080/api/books
     */
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            BookWithDetailsDTO createdBook = bookService.addBook(book);
            return ResponseEntity.status(201).body(createdBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while creating the book");
        }
    }
}