package com.eduardoxduardo.vlibrary.dto.request.create;

import com.eduardoxduardo.vlibrary.model.enums.ReadingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserBookCreateRequestDTO {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Reading status is required")
    private ReadingStatus readingStatus;
}
