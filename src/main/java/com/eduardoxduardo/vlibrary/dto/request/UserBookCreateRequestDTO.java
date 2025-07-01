package com.eduardoxduardo.vlibrary.dto.request;

import com.eduardoxduardo.vlibrary.model.enums.ReadingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBookCreateRequestDTO {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Reading status is required")
    private ReadingStatus readingStatus;
}
