package com.eduardoxduardo.vlibrary.mapper;

import com.eduardoxduardo.vlibrary.dto.response.AuthorResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper implements Mapper<AuthorResponseDTO, Author> {

    @Override
    public AuthorResponseDTO toDto(Author author) {

        if (author == null) {
            return null;
        }
        return new AuthorResponseDTO(
                author.getId(),
                author.getName()
        );
    }
}
