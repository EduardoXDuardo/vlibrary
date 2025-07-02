package com.eduardoxduardo.vlibrary.dto.request.update;

import com.eduardoxduardo.vlibrary.model.enums.ReadingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserBookUpdateReadingStatusRequestDTO {
    @NotNull(message = "Reading status is required")
    private ReadingStatus readingStatus;
}
