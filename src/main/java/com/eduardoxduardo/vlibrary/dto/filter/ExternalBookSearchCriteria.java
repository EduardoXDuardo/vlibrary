package com.eduardoxduardo.vlibrary.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalBookSearchCriteria {
    private String title;
    private String author;
    private String publisher;
    private String category;
    private String isbn;
    private String language;
}
