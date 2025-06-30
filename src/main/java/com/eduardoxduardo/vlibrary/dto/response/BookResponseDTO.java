package com.eduardoxduardo.vlibrary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String title;
    private AuthorResponseDTO author;
    private String description;
    private Set<GenreResponseDTO> genres;
}
