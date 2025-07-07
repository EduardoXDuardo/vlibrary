package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.create.BookCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.BookUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.BookMapper;
import com.eduardoxduardo.vlibrary.mapper.ReviewMapper;
import com.eduardoxduardo.vlibrary.mapper.UserMapper;
import com.eduardoxduardo.vlibrary.model.entities.*;
import com.eduardoxduardo.vlibrary.repository.AuthorRepository;
import com.eduardoxduardo.vlibrary.repository.BookRepository;
import com.eduardoxduardo.vlibrary.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;
    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;

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
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDto(books);
    }

    @Transactional(readOnly = true)
    public BookResponseDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAllBooksByAuthorId(Long id) {
        List<Book> books = bookRepository.findByAuthorId(id);
        return bookMapper.toDto(books);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAllBooksByGenreId(Long genreId) {
        List<Book> books = bookRepository.findByGenresId(genreId);
        return bookMapper.toDto(books);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> findAllReviewsByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));

        List<Review> reviews = book.getUserEntries().stream()
                .flatMap(userBook -> userBook.getReviews().stream())
                .toList();

        return reviewMapper.toDto(reviews);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsersByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));

        List<User> users = book.getUserEntries().stream()
                .map(UserBook::getUser)
                .distinct()
                .toList();

        return userMapper.toDto(users);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findBooksByTitle(String title) {
        if (title == null || title.isBlank()) {
            return List.of();
        }

        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);

        return bookMapper.toDto(books);
    }

    @Transactional
    public BookResponseDTO updateBook(Long bookId, BookUpdateRequestDTO request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            book.setTitle(request.getTitle());
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            book.setDescription(request.getDescription());
        }

        if (request.getAuthorId() != null) {
            Author newAuthor = authorRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new IllegalArgumentException("Author not found with ID: " + request.getAuthorId()));
            book.setAuthor(newAuthor);
        }


        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            Set<Genre> newGenres = request.getGenreIds().stream()
                    .map(genreId -> genreRepository.findById(genreId)
                            .orElseThrow(() -> new IllegalArgumentException("Genre not found with ID: " + genreId)))
                    .collect(Collectors.toSet());
            book.setGenres(newGenres);
        }

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }
}
