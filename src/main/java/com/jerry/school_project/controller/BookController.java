package com.jerry.school_project.controller;

import com.jerry.school_project.entity.Book;
import com.jerry.school_project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Get all books
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // Search by title (/api/books/search?title=pippi)
    @GetMapping("/books/search")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        List<Book> books = bookService.findBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    // Add a new book
    @PostMapping("/books")
    public ResponseEntity<Map<String, String>> addBook(@RequestBody Book book) {
        Map<String, String> response = bookService.addBook(book);

        if ("success".equals(response.get("status"))) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}