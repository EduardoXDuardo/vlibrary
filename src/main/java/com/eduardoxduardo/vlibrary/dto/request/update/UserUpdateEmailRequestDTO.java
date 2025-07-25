package com.eduardoxduardo.vlibrary.dto.request.update;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateEmailRequestDTO {
    @Email(message = "Email must be valid")
    private String email;
}
