package com.jerry.school_project.controller;

import com.jerry.school_project.entity.User;
import com.jerry.school_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/api/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        if (userRepository.existsByEmail(username)) {
            return "signup";
        }

        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, hashedPassword, "USER");
        userRepository.save(newUser);
        return "redirect:/login?registered";
    }
}
