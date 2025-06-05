package com.jerry.school_project.dto;

import java.time.LocalDateTime;

// DTO for displaying loan details with user and book information
public class LoanDetailsDTO {
    private Long loanId;
    private Long userId;
    private String userFullName;
    private Long bookId;
    private String bookTitle;
    private String authorName;
    private LocalDateTime borrowedDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnedDate;
    private boolean isOverdue;
    private boolean isActive;

    public LoanDetailsDTO() {}

    public LoanDetailsDTO(Long loanId, Long userId, String userFullName, Long bookId,
                          String bookTitle, String authorName, LocalDateTime borrowedDate,
                          LocalDateTime dueDate, LocalDateTime returnedDate) {
        this.loanId = loanId;
        this.userId = userId;
        this.userFullName = userFullName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
        this.isActive = (returnedDate == null);
        this.isOverdue = (returnedDate == null && dueDate != null && dueDate.isBefore(LocalDateTime.now()));
    }

    // Getters and setters
    public Long getLoanId() {
        return loanId;
    }
    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }
    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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
        // Update overdue status when due date changes
        this.isOverdue = (returnedDate == null && dueDate != null && dueDate.isBefore(LocalDateTime.now()));
    }

    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }
    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
        this.isActive = (returnedDate == null);
        // Update overdue status when returned date changes
        this.isOverdue = (returnedDate == null && dueDate != null && dueDate.isBefore(LocalDateTime.now()));
    }

    public boolean isOverdue() {
        return isOverdue;
    }
    public void setOverdue(boolean overdue) {
        this.isOverdue = overdue;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        this.isActive = active;
    }
}