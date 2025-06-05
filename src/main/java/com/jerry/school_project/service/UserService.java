package com.jerry.school_project.service;

import com.jerry.school_project.entity.User;
import com.jerry.school_project.dto.UserDTO;
import com.jerry.school_project.repository.UserRepository;
import com.jerry.school_project.mapper.UserMapper;
import com.jerry.school_project.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validation validation;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, Validation validation) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validation = validation;
    }

    // Get all users
    public List<UserDTO> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return userMapper.toUserDTOList(users);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Get user by email
    public UserDTO getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        try {
            Optional<User> userOpt = userRepository.findByEmail(email.trim());
            if (userOpt.isPresent()) {
                return userMapper.toUserDTO(userOpt.get());
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Database error occurred while searching for user", e);
        }
    }

    // Add a new user
    public UserDTO addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User data is required");
        }

        try {
            // Trim input values
            String firstName = user.getFirstName() != null ? user.getFirstName().trim() : null;
            String lastName = user.getLastName() != null ? user.getLastName().trim() : null;
            String email = user.getEmail() != null ? user.getEmail().trim() : null;
            String password = user.getPassword() != null ? user.getPassword().trim() : null;

            // Validation
            validation.validateString(firstName, "First name");
            validation.validateString(lastName, "Last name");

            String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            validation.validateString(email, "Email", true, false, false, emailPattern);
            validation.validateStringWithSpecialChars(password, "Password", true);

            // Check if user already exists with same email
            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("A user with this email already exists");
            }

            // Set the trimmed values back to the user object
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);

            // Set registration date to current date
            user.setRegistrationDate(LocalDateTime.now());

            User savedUser = userRepository.save(user);
            return userMapper.toUserDTO(savedUser);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user due to system error", e);
        }
    }
}