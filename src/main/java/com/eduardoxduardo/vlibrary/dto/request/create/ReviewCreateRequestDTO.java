package com.eduardoxduardo.vlibrary.dto.request.create;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCreateRequestDTO {

    @DecimalMax(value = "5.0", message = "Rating must be between 0 and 5")
    @DecimalMin(value = "0.0", message = "Rating must be between 0 and 5")
    private Double rating;
    private String comment;
}
