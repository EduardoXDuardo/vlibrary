package com.eduardoxduardo.vlibrary.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenreUpdateNameRequestDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;
}
