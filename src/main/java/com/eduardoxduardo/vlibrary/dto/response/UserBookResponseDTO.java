package com.eduardoxduardo.vlibrary.dto.response;

import com.eduardoxduardo.vlibrary.model.enums.ReadingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBookResponseDTO {
    private Long id;
    private ReadingStatus readingStatus;
    private BookResponseDTO book;
    private UserResponseDTO user;
    private List<ReviewResponseDTO> reviews;
}
