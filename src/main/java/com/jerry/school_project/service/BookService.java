package com.jerry.school_project.service;

import com.jerry.school_project.entity.Book;
import com.jerry.school_project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Search by title
    public List<Book> findBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return bookRepository.findByTitleContainingIgnoreCase(title.trim());
    }

    // Add a new book
    public Map<String, String> addBook(Book book) {
        Map<String, String> response = new HashMap<>();

        try {
            // Validate the book data
            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Book title is required");
                return response;
            }
            if (book.getTotalCopies() == null || book.getTotalCopies() < 0) {
                response.put("status", "error");
                response.put("message", "Total copies must be a positive number");
                return response;
            }

            // Set available copies to total copies if not specified
            if (book.getAvailableCopies() == null) {
                book.setAvailableCopies(book.getTotalCopies());
            }

            // Validate available copies doesn't exceed total copies
            if (book.getAvailableCopies() > book.getTotalCopies()) {
                response.put("status", "error");
                response.put("message", "Available copies cannot exceed total copies");
                return response;
            }

            Book savedBook = bookRepository.save(book);
            response.put("status", "success");
            response.put("message", "Book added successfully with ID: " + savedBook.getId());

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
        }

        return response;
    }

}
