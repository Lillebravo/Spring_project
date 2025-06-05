package com.jerry.school_project.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", columnDefinition = "INTEGER")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "available_copies")
    private Integer availableCopies;

    @Column(name = "total_copies")
    private Integer totalCopies;

    @Column(name = "author_id", columnDefinition = "INTEGER")
    private Long authorId;

    // Relationships - One book can have many loans
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Loan> loans;

    public Book() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }
    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }
    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public List<Loan> getLoans() { return loans; }
    public void setLoans(List<Loan> loans) { this.loans = loans; }
}