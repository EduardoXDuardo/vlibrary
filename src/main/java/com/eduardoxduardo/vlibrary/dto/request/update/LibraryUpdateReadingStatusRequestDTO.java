package com.eduardoxduardo.vlibrary.dto.request.update;

import com.eduardoxduardo.vlibrary.model.enums.ReadingStatus;
import lombok.Data;

@Data
public class LibraryUpdateReadingStatusRequestDTO {
    private ReadingStatus readingStatus;
}
