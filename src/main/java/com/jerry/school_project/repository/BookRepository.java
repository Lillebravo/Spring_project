package com.jerry.school_project.repository;

import com.jerry.school_project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Search books by title or author name
    @Query("SELECT b FROM Book b JOIN Author a ON b.authorId = a.id WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    List<Book> searchBooksByTitleOrAuthor(@Param("searchQuery") String searchQuery);

    // Check if a book with the same title and author already exists
    boolean existsByTitleAndAuthorId(String title, Long authorId);
}