package com.eduardoxduardo.vlibrary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookExternalResponseDTO {
    String apiId;
    String title;
    List<String> authors;
    String description;
    List<String> categories;
}
