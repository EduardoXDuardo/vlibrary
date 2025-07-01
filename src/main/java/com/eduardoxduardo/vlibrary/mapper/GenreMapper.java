package com.eduardoxduardo.vlibrary.mapper;

import com.eduardoxduardo.vlibrary.dto.response.GenreResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper implements Mapper<GenreResponseDTO, Genre> {

    @Override
    public GenreResponseDTO toDto(Genre genre) {

        if (genre == null) {
            return null;
        }

        return new GenreResponseDTO(
                genre.getId(),
                genre.getName()
        );
    }
}
