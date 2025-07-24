package com.eduardoxduardo.vlibrary.client;

import com.eduardoxduardo.vlibrary.dto.filter.ExternalBookSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.response.BookExternalResponseDTO;

import java.util.List;

public interface BooksClient {
    List<BookExternalResponseDTO> searchBooks(ExternalBookSearchCriteria criteria);
    BookExternalResponseDTO findBookById(String apiId);
}
