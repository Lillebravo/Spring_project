package com.jerry.school_project;

import com.jerry.school_project.dto.LoanDetailsDTO;
import com.jerry.school_project.entity.Author;
import com.jerry.school_project.entity.Book;
import com.jerry.school_project.entity.Loan;
import com.jerry.school_project.entity.User;
import com.jerry.school_project.mapper.LoanMapper;
import com.jerry.school_project.repository.AuthorRepository;
import com.jerry.school_project.repository.BookRepository;
import com.jerry.school_project.repository.LoanRepository;
import com.jerry.school_project.repository.UserRepository;
import com.jerry.school_project.service.LoanService;
import com.jerry.school_project.util.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private Validation validation;

    @InjectMocks
    private LoanService loanService;

    private User testUser;
    private Book testBook;
    private Author testAuthor;
    private Loan testLoan;
    private LoanDetailsDTO testLoanDetailsDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");

        testAuthor = new Author();
        testAuthor.setId(1L);
        testAuthor.setFirstName("Test");
        testAuthor.setLastName("Author");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthorId(1L);
        testBook.setAvailableCopies(5);

        testLoan = new Loan();
        testLoan.setId(1L);
        testLoan.setUserId(1L);
        testLoan.setBookId(1L);
        testLoan.setBorrowedDate(LocalDateTime.now());
        testLoan.setDueDate(LocalDateTime.now().plusDays(14));

        testLoanDetailsDTO = new LoanDetailsDTO();
        testLoanDetailsDTO.setLoanId(1L);
        testLoanDetailsDTO.setUserId(1L);
        testLoanDetailsDTO.setBookId(1L);
        testLoanDetailsDTO.setBorrowedDate(LocalDateTime.now());
        testLoanDetailsDTO.setDueDate(LocalDateTime.now().plusDays(14));
    }

    @Test
    void testLoanBook_SetsCorrectDueDate() {
        // Arrange
        Long userId = 1L;
        Long bookId = 1L;

        // Mock repository responses
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
        when(authorRepository.findById(testBook.getAuthorId())).thenReturn(Optional.of(testAuthor));

        // Capture the loan that gets saved
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
            Loan savedLoan = invocation.getArgument(0);
            savedLoan.setId(1L); // Simulate auto-generated ID
            return savedLoan;
        });

        when(loanMapper.toLoanDetailsDTO(any(Loan.class), any(User.class), any(Book.class), any(Author.class)))
                .thenReturn(testLoanDetailsDTO);

        // Record the time before making the loan
        LocalDateTime beforeLoan = LocalDateTime.now();

        // Act
        LoanDetailsDTO result = loanService.loanBook(userId, bookId);

        // Assert
        assertNotNull(result, "Loan should be created successfully");

        // Verify that a loan was saved with correct due date
        verify(loanRepository).save(argThat(loan -> {
            // Check that borrowed date is set to now (within 1 second tolerance)
            LocalDateTime borrowedDate = loan.getBorrowedDate();
            assertNotNull(borrowedDate, "Borrowed date should be set");
            assertTrue(Math.abs(ChronoUnit.SECONDS.between(beforeLoan, borrowedDate)) <= 1,
                    "Borrowed date should be approximately now");

            // Check that due date is exactly 14 days after borrowed date
            LocalDateTime dueDate = loan.getDueDate();
            assertNotNull(dueDate, "Due date should be set");
            assertEquals(14, ChronoUnit.DAYS.between(borrowedDate, dueDate),
                    "Due date should be exactly 14 days after borrowed date");

            // Check that returned date is null (not returned yet)
            assertNull(loan.getReturnedDate(), "Returned date should be null for new loan");

            return true;
        }));

        // Verify that book's available copies were decremented
        verify(bookRepository).save(argThat(book -> {
            assertEquals(4, book.getAvailableCopies(),
                    "Available copies should be decremented by 1");
            return true;
        }));
    }

    @Test
    void testLoanBook_FailsWhenNoAvailableCopies() {
        // Arrange
        Long userId = 1L;
        Long bookId = 1L;

        // Set book to have 0 available copies
        testBook.setAvailableCopies(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loanService.loanBook(userId, bookId),
                "Should throw IllegalArgumentException when no copies available"
        );

        assertEquals("No available copies of this book", exception.getMessage(),
                "Exception message should indicate no available copies");

        // Verify that no loan was saved
        verify(loanRepository, never()).save(any(Loan.class));

        // Verify that book's available copies were not modified
        verify(bookRepository, never()).save(any(Book.class));
    }
}