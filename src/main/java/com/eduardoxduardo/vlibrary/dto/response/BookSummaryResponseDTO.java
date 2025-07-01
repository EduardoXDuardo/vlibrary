package com.eduardoxduardo.vlibrary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSummaryResponseDTO {
    private Long id;
    private String title;
    private String author;
}
