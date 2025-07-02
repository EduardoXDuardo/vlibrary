package com.eduardoxduardo.vlibrary.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewUpdateMessageRequestDTO {
    @NotBlank(message = "Message cannot be blank")
    private String message;
}
