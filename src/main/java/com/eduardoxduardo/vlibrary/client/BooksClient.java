package com.eduardoxduardo.vlibrary.client;

import com.eduardoxduardo.vlibrary.dto.response.BookExternalResponseDTO;

import java.util.List;

public interface BooksClient {
    List<BookExternalResponseDTO> searchBooksByTitle(String title);
    BookExternalResponseDTO findBookById(String apiId);
}
