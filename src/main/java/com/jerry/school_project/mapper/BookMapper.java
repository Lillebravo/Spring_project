package com.jerry.school_project.mapper;

import com.jerry.school_project.dto.BookWithDetailsDTO;
import com.jerry.school_project.entity.Book;
import com.jerry.school_project.entity.Author;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    // Convert a Book entity with Author entity to BookWithDetailsDTO
    public BookWithDetailsDTO toBookWithDetailsDTO(Book book, Author author) {
        if (book == null) {
            return null;
        }

        String authorName = null;
        if (author != null) {
            authorName = (author.getFirstName() != null ? author.getFirstName() : "") +
                    " " +
                    (author.getLastName() != null ? author.getLastName() : "");
            authorName = authorName.trim();
            if (authorName.isEmpty()) {
                authorName = null;
            }
        }

        return new BookWithDetailsDTO(
                book.getId(),
                book.getTitle(),
                book.getPublicationYear(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                book.getAuthorId(),
                authorName,
                author != null ? author.getBirthYear() : null,
                author != null ? author.getNationality() : null
        );
    }

    // Convert a list of Book entities with their corresponding Author entities to BookWithDetailsDTO list
    public List<BookWithDetailsDTO> toBookWithDetailsDTOList(List<Book> books, List<Author> authors) {
        if (books == null || books.isEmpty()) {
            return List.of();
        }

        return books.stream()
                .map(book -> {
                    Author author = authors != null ?
                            authors.stream()
                                    .filter(a -> a.getId().equals(book.getAuthorId()))
                                    .findFirst()
                                    .orElse(null) : null;
                    return toBookWithDetailsDTO(book, author);
                })
                .collect(Collectors.toList());
    }
}