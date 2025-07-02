package com.eduardoxduardo.vlibrary.dto.request.update;

import lombok.Data;

import java.util.Set;

@Data
public class BookUpdateRequestDTO {
    private String title;
    private String description;
    private Set<Long> genreIds;
    private Long authorId;
}
