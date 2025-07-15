package com.eduardoxduardo.vlibrary.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchCriteria {
    private String title;
    private Long authorId;
    private Long genreId;
}
