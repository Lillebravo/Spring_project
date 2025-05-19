package com.jerry.school_project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "borrowed_date")
    private String borrowedDate;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "returned_date")
    private String returnedDate;

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

    public String getBorrowedDate() {
        return borrowedDate;
    }
    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getReturnedDate() {
        return returnedDate;
    }
    public void setReturnedDate(String returnedDate) {
        this.returnedDate = returnedDate;
    }
}
