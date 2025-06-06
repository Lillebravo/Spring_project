package com.jerry.school_project.dto;

// DTO for displaying author full name
public class AuthorDTO {
    private Long id;
    private String name;
    private Integer birthYear;
    private String nationality;

    public AuthorDTO() {}

    public AuthorDTO(Long id, String firstName, String lastName, Integer birthYear, String nationality) {
        this.id = id;
        this.name = firstName + " " + lastName;
        this.birthYear = birthYear;
        this.nationality = nationality;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return name; }
    public void setFullName(String firstName, String lastName) {
        this.name = firstName + " " + lastName;
    }

    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
}