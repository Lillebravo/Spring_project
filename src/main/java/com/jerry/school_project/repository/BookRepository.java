package com.jerry.school_project.repository;

import com.jerry.school_project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find books by title (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);
}