package com.eduardoxduardo.vlibrary.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewResponseDTO {
    private Long id;
    private Double rating;
    private String comment;
    private LocalDate reviewDate;
}
