package com.eduardoxduardo.vlibrary.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data

public class BookCreateRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    private Set<Long> genreIds;

    private String description;
}
