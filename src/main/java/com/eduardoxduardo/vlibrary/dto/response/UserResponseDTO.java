package com.eduardoxduardo.vlibrary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User information response")
public class UserResponseDTO {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;
    
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
}
