package com.eduardoxduardo.vlibrary.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdatePasswordRequestDTO {
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;
}
