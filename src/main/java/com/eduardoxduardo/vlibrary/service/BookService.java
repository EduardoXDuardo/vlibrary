package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.BookCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.BookMapper;
import com.eduardoxduardo.vlibrary.model.entities.Author;
import com.eduardoxduardo.vlibrary.model.entities.Book;
import com.eduardoxduardo.vlibrary.model.entities.Genre;
import com.eduardoxduardo.vlibrary.repository.AuthorRepository;
import com.eduardoxduardo.vlibrary.repository.BookRepository;
import com.eduardoxduardo.vlibrary.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public BookResponseDTO createBook(BookCreateRequestDTO request) {

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Set<Genre> genres = request.getGenreIds().stream()
                .map(genreId -> genreRepository.findById(genreId)
                        .orElseThrow(() -> new IllegalArgumentException("Genre not found with ID: " + genreId)))
                .collect(Collectors.toSet());

        if (genres.isEmpty()) {
            throw new IllegalArgumentException("At least one valid genre is required");
        }

        Book newBook = new Book(request.getTitle(), author);
        newBook.setDescription(request.getDescription());
        newBook.setGenres(genres);

        Book savedBook = bookRepository.save(newBook);

        return bookMapper.toDto(savedBook);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAllBooks() {
    }

    @Transactional(readOnly = true)
    public BookResponseDTO findBookById(Long id) {
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAllBooksByAuthorId(Long id) {
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAllBooksByGenreId(Long id) {
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> findAllReviewsByBookId(Long bookId) {
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findBooksByTitle(String title) {
    }
}
