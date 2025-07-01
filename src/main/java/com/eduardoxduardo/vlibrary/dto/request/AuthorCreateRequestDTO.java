package com.eduardoxduardo.vlibrary.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCreateRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;
}
