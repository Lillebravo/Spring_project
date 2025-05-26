package com.jerry.school_project.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id", columnDefinition = "INTEGER")
    private Long id;

    @Column(name = "user_id", columnDefinition = "INTEGER")
    private Long userId;

    @Column(name = "book_id", columnDefinition = "INTEGER")
    private Long bookId;

    @Column(name = "borrowed_date")
    private LocalDateTime borrowedDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    // Default constructor
    public Loan() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getBorrowedDate() {
        return borrowedDate;
    }
    public void setBorrowedDate(LocalDateTime borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }
    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }
}
