package com.eduardoxduardo.vlibrary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User login request")
public class LoginRequestDTO {
    @NotBlank(message = "Username or email is required")
    @Schema(description = "Username or email address", example = "john_doe")
    private String usernameOrEmail;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password123")
    private String password;
}
