package com.eduardoxduardo.vlibrary.dto.request.update;

import com.eduardoxduardo.vlibrary.model.entities.Genre;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class BookUpdateGenresRequestDTO {
    @NotEmpty(message = "Genres cannot be empty")
    private Set<Genre> genres;
}
