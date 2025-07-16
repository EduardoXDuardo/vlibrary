package com.eduardoxduardo.vlibrary.dto.filter;

import com.eduardoxduardo.vlibrary.model.enums.ReadingStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibrarySearchCriteria {
    private Long userId;
    private String bookTitle;
    private Long authorId;
    private Long genreId;
    @DecimalMax(value = "5.0", message = "Rating must be between 0 and 5")
    @DecimalMin(value = "0.0", message = "Rating must be between 0 and 5")
    private Double rating;
    private ReadingStatus status;
}
