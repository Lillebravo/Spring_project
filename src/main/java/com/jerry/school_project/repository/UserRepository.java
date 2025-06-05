package com.jerry.school_project.repository;

import com.jerry.school_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if a user with the same email already exists
    boolean existsByEmail(String email);
}