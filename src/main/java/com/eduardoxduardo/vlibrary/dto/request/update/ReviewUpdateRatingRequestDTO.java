package com.eduardoxduardo.vlibrary.dto.request.update;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class ReviewUpdateRatingRequestDTO {
    @DecimalMax(value = "5.0", message = "Rating must be between 0 and 5")
    @DecimalMin(value = "0.0", message = "Rating must be between 0 and 5")
    private Double rating;
}
