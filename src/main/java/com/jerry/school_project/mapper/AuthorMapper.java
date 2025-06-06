package com.jerry.school_project.mapper;

import com.jerry.school_project.dto.AuthorDTO;
import com.jerry.school_project.entity.Author;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper {

    // Convert Author entity to AuthorDTO (combining firstName and lastName into full name)
    public AuthorDTO toAuthorDTO(Author author) {
        if (author == null) {
            return null;
        }

        return new AuthorDTO(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthYear(),
                author.getNationality()
        );
    }

    // Convert list of Author entities to list of AuthorDTOs
    public List<AuthorDTO> toAuthorDTOList(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return new ArrayList<>();
        }

        List<AuthorDTO> authorDTOs = new ArrayList<>();
        for (Author author : authors) {
            AuthorDTO authorDTO = toAuthorDTO(author);
            if (authorDTO != null) {
                authorDTOs.add(authorDTO);
            }
        }
        return authorDTOs;
    }
}