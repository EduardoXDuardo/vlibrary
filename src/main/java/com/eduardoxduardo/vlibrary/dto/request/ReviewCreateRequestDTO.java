package com.eduardoxduardo.vlibrary.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequestDTO {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    private Double rating;
    private String comment;
}
