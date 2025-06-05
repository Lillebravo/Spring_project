package com.jerry.school_project.controller;

import com.jerry.school_project.dto.UserDTO;
import com.jerry.school_project.entity.User;
import com.jerry.school_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users
     * Example: GET localhost:8080/api/users
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.status(200).body(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while retrieving users");
        }
    }

    /**
     * Get user by email
     * Example: GET localhost:8080/api/users/email/john@example.com
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            UserDTO user = userService.getUserByEmail(email);

            if (user == null) {
                return ResponseEntity.status(404).body("No user found with email: " + email);
            }

            return ResponseEntity.status(200).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while searching for user");
        }
    }

    /**
     * Add a new user
     * Example: POST localhost:8080/api/users
     */
    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            UserDTO createdUser = userService.addUser(user);
            return ResponseEntity.status(201).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while creating the user");
        }
    }
}