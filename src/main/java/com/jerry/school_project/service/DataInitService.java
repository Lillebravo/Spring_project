package com.jerry.school_project.service;

import com.jerry.school_project.entity.User;
import com.jerry.school_project.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {
        if (userRepository.findByEmail("user@test.com").isEmpty()) {
            User user = new User();
            user.setFirstName("user");
            user.setLastName("userson");
            user.setEmail("user@test.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("USER");
            userRepository.save(user);
        }

        if (userRepository.findByEmail("admin@test.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("admin");
            admin.setLastName("adminson");
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        System.out.println("USER IS CREATED");
        System.out.println("ADMIN IS CREATEDEDEDEDED!");
    }
}
