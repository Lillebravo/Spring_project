package com.jerry.school_project.service;

import com.jerry.school_project.PasswordWeakException;
import com.jerry.school_project.entity.User;
import com.jerry.school_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User Details Service Implementation
 * Loads user details from database for Spring Security authentication
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Load user details by email for authentication
     * Converts database User entity to Spring Security UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, PasswordWeakException {
        Optional<User> user = userRepository.findByEmail(email);

        // Check if user exists
        if (user == null || !user.isPresent()) {
            throw new UsernameNotFoundException(email);
        }

        // Convert database User to Spring Security UserDetails
        return user.map(value -> org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(value.getPassword()) // Encrypted password from database
                .authorities("ROLE_" + value.getRole()).build()).orElse(null); // Add "ROLE_" prefix to user's role
    }

}
