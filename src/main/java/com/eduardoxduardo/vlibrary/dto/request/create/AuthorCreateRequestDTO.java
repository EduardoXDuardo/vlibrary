package com.eduardoxduardo.vlibrary.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorCreateRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;
}
