package com.eduardoxduardo.vlibrary.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookUpdateTitleRequestDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;
}
