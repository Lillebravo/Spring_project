package com.jerry.school_project.dto;

public class BookWithDetailsDTO {
    private Long id;
    private String title;
    private Integer publicationYear;
    private Integer availableCopies;
    private Integer totalCopies;
    private Long authorId;
    private String authorName;
    private Integer authorBirthYear;
    private String authorNationality;

    // Default constructor
    public BookWithDetailsDTO() {}

    // Constructor
    public BookWithDetailsDTO(Long id, String title, Integer publicationYear, Integer availableCopies,
                              Integer totalCopies, Long authorId, String authorName,
                              Integer authorBirthYear, String authorNationality) {
        this.id = id;
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorBirthYear = authorBirthYear;
        this.authorNationality = authorNationality;
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

    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getAuthorBirthYear() {
        return authorBirthYear;
    }
    public void setAuthorBirthYear(Integer authorBirthYear) {
        this.authorBirthYear = authorBirthYear;
    }

    public String getAuthorNationality() {
        return authorNationality;
    }
    public void setAuthorNationality(String authorNationality) {
        this.authorNationality = authorNationality;
    }
}