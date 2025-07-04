package com.eduardoxduardo.vlibrary.mapper;

import com.eduardoxduardo.vlibrary.dto.response.AuthorResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookSummaryResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.GenreResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookMapper implements Mapper<BookResponseDTO, Book> {

    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;

    public BookResponseDTO toDto(Book book) {
        if (book == null) {
            return null;
        }

        AuthorResponseDTO authorDTO = authorMapper.toDto(book.getAuthor());
        Set<GenreResponseDTO> genresDTO = genreMapper.toDto(book.getGenres());

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                authorDTO,
                book.getDescription(),
                genresDTO
        );
    }

    public BookSummaryResponseDTO toSummaryDto(Book book) {
        if (book == null) {
            return null;
        }

        return new BookSummaryResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName()
        );
    }
}
