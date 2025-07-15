package com.eduardoxduardo.vlibrary.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User registration request")
public class UserRegisterRequestDTO {

    @NotBlank(message = "Username is required")
    @Schema(description = "Username for the new user", example = "john_doe")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Schema(description = "Password for the new user", example = "password123")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address for the new user", example = "john.doe@example.com")
    private String email;
}
