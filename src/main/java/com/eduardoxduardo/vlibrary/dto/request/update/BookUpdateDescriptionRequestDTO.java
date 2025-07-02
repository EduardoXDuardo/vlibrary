package com.eduardoxduardo.vlibrary.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookUpdateDescriptionRequestDTO {
    @NotBlank(message = "Description cannot be blank")
    private String description;
}
