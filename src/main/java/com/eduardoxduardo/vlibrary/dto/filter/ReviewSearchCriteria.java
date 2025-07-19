package com.eduardoxduardo.vlibrary.dto.filter;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSearchCriteria {
    private Long userId;
    private Long bookId;
    private String commentContains;
    @DecimalMax(value = "5.0", message = "Rating must be between 0 and 5")
    @DecimalMin(value = "0.0", message = "Rating must be between 0 and 5")
    private Double rating;
}
