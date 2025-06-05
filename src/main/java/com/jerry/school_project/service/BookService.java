package com.jerry.school_project.service;

import com.jerry.school_project.entity.Book;
import com.jerry.school_project.entity.Author;
import com.jerry.school_project.dto.BookWithDetailsDTO;
import com.jerry.school_project.repository.BookRepository;
import com.jerry.school_project.repository.AuthorRepository;
import com.jerry.school_project.mapper.BookMapper;
import com.jerry.school_project.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final Validation validation;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper, Validation validation) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
        this.validation = validation;
    }

    // Get all books
    public List<BookWithDetailsDTO> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();
            return convertBookListToDTO(books);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Search books by title or author name
    public List<BookWithDetailsDTO> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        try {
            List<Book> books = bookRepository.searchBooksByTitleOrAuthor(query.trim());
            return convertBookListToDTO(books);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search books", e);
        }
    }

    // Add new book
    public BookWithDetailsDTO addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book data is required");
        }

        String title = book.getTitle() != null ? book.getTitle().trim() : null;

        // Validation
        validation.validateStringWithSpecialChars(title, "Book title", true);
        validation.validateLong(book.getAuthorId(), "Author ID", 1L, null);
        validation.validateInteger(book.getAvailableCopies(), "Available copies");
        validation.validateInteger(book.getTotalCopies(), "Total copies");
        validation.validatePublicationYear(book.getPublicationYear());

        // Validate that the author exists
        Optional<Author> authorOpt = authorRepository.findById(book.getAuthorId());
        if (authorOpt.isEmpty()) {
            throw new IllegalArgumentException("Author with ID " + book.getAuthorId() + " does not exist");
        }

        // Set available copies to total copies if not specified
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }

        // Validate relationship between available and total copies
        validation.validateIntegerRelationship(book.getAvailableCopies(), book.getTotalCopies(),
                "Available copies", "total copies", "not_exceed");

        // Check if book already exists with same title and author
        if (bookRepository.existsByTitleAndAuthorId(title, book.getAuthorId())) {
            throw new IllegalArgumentException("A book with this title and author already exists");
        }

        try {
            Book savedBook = bookRepository.save(book);
            Author author = authorOpt.get();
            return bookMapper.toBookWithDetailsDTO(savedBook, author);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create book due to system error", e);
        }
    }

    // Helper method to convert Book entities to DTOs
    private List<BookWithDetailsDTO> convertBookListToDTO(List<Book> books) {
        if (books == null || books.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Get all unique author IDs
            List<Long> authorIds = books.stream()
                    .map(Book::getAuthorId)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());

            // Get all authors
            List<Author> authors = authorRepository.findAllById(authorIds);

            // Convert using mapper
            return bookMapper.toBookWithDetailsDTOList(books, authors);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert books to DTOs", e);
        }
    }
}