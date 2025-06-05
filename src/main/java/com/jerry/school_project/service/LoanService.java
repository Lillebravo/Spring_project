package com.jerry.school_project.service;

import com.jerry.school_project.entity.Loan;
import com.jerry.school_project.entity.Book;
import com.jerry.school_project.entity.User;
import com.jerry.school_project.entity.Author;
import com.jerry.school_project.dto.UserLoanDTO;
import com.jerry.school_project.dto.LoanDetailsDTO;
import com.jerry.school_project.repository.LoanRepository;
import com.jerry.school_project.repository.BookRepository;
import com.jerry.school_project.repository.UserRepository;
import com.jerry.school_project.repository.AuthorRepository;
import com.jerry.school_project.mapper.LoanMapper;
import com.jerry.school_project.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final LoanMapper loanMapper;
    private final Validation validation;

    @Autowired
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository,
                       UserRepository userRepository, AuthorRepository authorRepository,
                       LoanMapper loanMapper, Validation validation) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.loanMapper = loanMapper;
        this.validation = validation;
    }

    // Get user loans
    public List<UserLoanDTO> getUserLoans(Long userId) {
        validation.validateLong(userId, "User ID", 1L, null);

        try {
            List<Loan> loans = loanRepository.findByUserId(userId);
            if (loans.isEmpty()) {
                return List.of();
            }

            // Get all unique book IDs
            List<Long> bookIds = loans.stream()
                    .map(Loan::getBookId)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());

            // Get all books
            List<Book> books = bookRepository.findAllById(bookIds);

            // Get all unique author IDs
            List<Long> authorIds = books.stream()
                    .map(Book::getAuthorId)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());

            // Get all authors
            List<Author> authors = authorRepository.findAllById(authorIds);

            return loanMapper.toUserLoanDTOList(loans, books, authors);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve user loans", e);
        }
    }

    // Loan book
    public LoanDetailsDTO loanBook(Long userId, Long bookId) {
        if (userId == null || bookId == null) {
            throw new IllegalArgumentException("User ID and Book ID are required");
        }

        validation.validateLong(userId, "User ID", 1L, null);
        validation.validateLong(bookId, "Book ID", 1L, null);

        // Check if user exists
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist");
        }

        // Check if book exists
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book with ID " + bookId + " does not exist");
        }

        Book book = bookOpt.get();

        // Check if book has available copies
        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException("No available copies of this book");
        }

        try {
            // Create new loan
            Loan loan = new Loan();
            loan.setUserId(userId);
            loan.setBookId(bookId);
            loan.setBorrowedDate(LocalDateTime.now());
            loan.setDueDate(LocalDateTime.now().plusDays(14)); // 14 days loan period
            loan.setReturnedDate(null); // Not returned yet

            // Save loan
            Loan savedLoan = loanRepository.save(loan);

            // Update book available copies
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepository.save(book);

            // Get author for DTO
            Optional<Author> authorOpt = authorRepository.findById(book.getAuthorId());
            if (authorOpt.isEmpty()) {
                throw new RuntimeException("Author not found for book ID " + bookId);
            }

            // Convert to DTO
            return loanMapper.toLoanDetailsDTO(savedLoan, userOpt.get(), book, authorOpt.get());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create loan due to system error", e);
        }
    }

    // Return a book
    public LoanDetailsDTO returnBook(Long loanId) {
        if (loanId == null) {
            throw new IllegalArgumentException("Loan ID is required");
        }

        validation.validateLong(loanId, "Loan ID", 1L, null);

        // Find the loan
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) {
            throw new IllegalArgumentException("Loan with ID " + loanId + " does not exist");
        }

        Loan loan = loanOpt.get();

        // Check if already returned
        if (loan.getReturnedDate() != null) {
            throw new IllegalArgumentException("This book has already been returned");
        }

        try {
            // Mark as returned
            loan.setReturnedDate(LocalDateTime.now());
            Loan savedLoan = loanRepository.save(loan);

            // Update book available copies
            Optional<Book> bookOpt = bookRepository.findById(loan.getBookId());
            if (bookOpt.isEmpty()) {
                throw new RuntimeException("Book not found for loan ID " + loanId);
            }

            Book book = bookOpt.get();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);

            // Get user and author for DTO
            Optional<User> userOpt = userRepository.findById(loan.getUserId());
            Optional<Author> authorOpt = authorRepository.findById(book.getAuthorId());

            if (userOpt.isEmpty()) {
                throw new RuntimeException("User not found for loan ID " + loanId);
            }
            if (authorOpt.isEmpty()) {
                throw new RuntimeException("Author not found for loan ID " + loanId);
            }

            return loanMapper.toLoanDetailsDTO(savedLoan, userOpt.get(), book, authorOpt.get());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to return book due to system error", e);
        }
    }

    // Extend loan
    public LoanDetailsDTO extendLoan(Long loanId) {
        if (loanId == null) {
            throw new IllegalArgumentException("Loan ID is required");
        }

        validation.validateLong(loanId, "Loan ID", 1L, null);

        // Find the loan
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) {
            throw new IllegalArgumentException("Loan with ID " + loanId + " does not exist");
        }

        Loan loan = loanOpt.get();

        // Check if already returned
        if (loan.getReturnedDate() != null) {
            throw new IllegalArgumentException("Cannot extend a returned book loan");
        }

        // Check if loan is already overdue
        if (loan.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot extend an overdue loan");
        }

        try {
            // Extend due date by 14 days
            loan.setDueDate(loan.getDueDate().plusDays(14));
            Loan savedLoan = loanRepository.save(loan);

            // Get user, book, and author for DTO
            Optional<User> userOpt = userRepository.findById(loan.getUserId());
            Optional<Book> bookOpt = bookRepository.findById(loan.getBookId());

            if (userOpt.isEmpty()) {
                throw new RuntimeException("User not found for loan ID " + loanId);
            }
            if (bookOpt.isEmpty()) {
                throw new RuntimeException("Book not found for loan ID " + loanId);
            }

            Optional<Author> authorOpt = authorRepository.findById(bookOpt.get().getAuthorId());
            if (authorOpt.isEmpty()) {
                throw new RuntimeException("Author not found for loan ID " + loanId);
            }

            return loanMapper.toLoanDetailsDTO(savedLoan, userOpt.get(), bookOpt.get(), authorOpt.get());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extend loan due to system error", e);
        }
    }
}