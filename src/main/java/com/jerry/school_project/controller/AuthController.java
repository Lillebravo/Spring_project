package com.jerry.school_project.controller;

import com.jerry.school_project.entity.LoginRequest;
import com.jerry.school_project.entity.SignUpRequest;
import com.jerry.school_project.entity.User;
import com.jerry.school_project.repository.UserRepository;
import com.jerry.school_project.service.JwtBlacklistService;
import com.jerry.school_project.util.JwtResonse;
import com.jerry.school_project.util.JwtUtils;
import com.jerry.school_project.util.Validation;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JwtBlacklistService jwtBlacklistService;
    @Autowired
    private Validation validation;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getUsername()).isPresent()) {
            LOGGER.info("User {} already exists", signUpRequest.getUsername());
            return ResponseEntity.badRequest().body("Error: user already exists");
        }

        try {
            validation.validatePassword(signUpRequest.getPassword());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }

        User user = new User();
        user.setFirstName("user");
        user.setLastName("userson");
        user.setEmail(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            LOGGER.info("LOGIN_SUCCESS: user={}, ip={}, path={}", loginRequest.getUsername(), request.getRemoteAddr(), request.getRequestURI());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResonse(jwt, userDetails.getUsername(), roles));
        } catch (Exception e) {
            LOGGER.warn("LOGIN_FAILED: user={}, ip={}, path={}", loginRequest.getUsername(), request.getRemoteAddr(), request.getRequestURI());
            throw e; // handled by AuthEntryPointJwt
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authHeader, HttpServletRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Error: Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Date exp = jwtUtils.getExpirationFromJwtToken(token);

        jwtBlacklistService.blacklistToken(token, exp);

        LOGGER.info("LOGOUT: ip={}, path={}", request.getRemoteAddr(), request.getRequestURI());

        return ResponseEntity.ok("User logged out successfully");
    }
}
