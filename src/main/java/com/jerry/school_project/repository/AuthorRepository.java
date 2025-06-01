package com.jerry.school_project.repository;

import com.jerry.school_project.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Find authors by last name (case insensitive)
    @Query("SELECT a FROM Author a WHERE LOWER(a.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Author> findByLastName(@Param("lastName") String lastName);

    // Check if an author with the same first name and last name already exists
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}