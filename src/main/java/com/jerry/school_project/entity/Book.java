package com.jerry.school_project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books")  // Måste matcha tabellnamnet i databasen
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")  // Måste matcha kolumnnamnet i DB
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "available_copies")
    private Integer availableCopies;

    @Column(name = "total_copies")
    private Integer totalCopies;

    @Column(name = "author_id")
    private Long authorId;

    // Default constructor demanded by JPA
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

    public Long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
