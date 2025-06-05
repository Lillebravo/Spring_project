package com.jerry.school_project.dto;

import java.time.LocalDateTime;

// DTO for displaying user without password
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime registrationDate;

    public UserDTO() {}

    public UserDTO(Long id, String firstName, String lastName, String email, LocalDateTime registrationDate) {
        this.id = id;
        this.name = firstName + " " + lastName;
        this.email = email;
        this.registrationDate = registrationDate;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return name; }
    public void setFullName(String firstName, String lastName) {
        this.name = firstName + " " + lastName;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
}
