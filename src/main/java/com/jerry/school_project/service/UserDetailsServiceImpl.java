package com.jerry.school_project.service;


import com.jerry.school_project.PasswordWeakException;
import com.jerry.school_project.entity.User;
import com.jerry.school_project.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, PasswordWeakException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user == null || !user.isPresent()) {
            throw new UsernameNotFoundException(email);
        }
        if (user.get().getPassword().length() < 8) {
            throw new PasswordWeakException("The password is too weak, less than 8 characters");
        }

        return user.map(value -> org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(value.getPassword())
                .authorities("ROLE" + value.getRole()).build()).orElse(null);
    }

}
