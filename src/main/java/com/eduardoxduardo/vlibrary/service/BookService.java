package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.client.BooksClient;
import com.eduardoxduardo.vlibrary.dto.filter.BookSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.filter.ExternalBookSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.create.BookCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.BookUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookExternalResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.BookMapper;
import com.eduardoxduardo.vlibrary.model.entities.*;
import com.eduardoxduardo.vlibrary.repository.AuthorRepository;
import com.eduardoxduardo.vlibrary.repository.BookRepository;
import com.eduardoxduardo.vlibrary.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;
    private final BooksClient booksClient;

    // Only create a new book manually if it does not exist in the book api
    @Transactional
    public BookResponseDTO createBook(BookCreateRequestDTO request) {
        Book newBook = new Book();
        newBook.setTitle(request.getTitle());
        newBook.setDescription(request.getDescription());

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        newBook.setAuthor(author);

        Set<Genre> genres = request.getGenreIds().stream()
                .map(genreId -> genreRepository.findById(genreId)
                        .orElseThrow(() -> new IllegalArgumentException("Genre not found with ID: " + genreId)))
                .collect(Collectors.toSet());
        newBook.setGenres(genres);

        Book savedBook = bookRepository.save(newBook);

        return bookMapper.toDto(savedBook);
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> findBooks(BookSearchCriteria criteria, int page, int size, String sortBy, String sortDirection) {
        Specification<Book> spec = createSpecification(criteria);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Book> books = bookRepository.findAll(spec, pageRequest);
        return books.map(bookMapper::toDto);
    }

    @Transactional(readOnly = true)
    public BookResponseDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
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

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with ID: " + id);
        }

        if (bookRepository.existsByUserEntries_BookId(id)) {
            throw new IllegalStateException("Cannot delete book with existing user entries");
        }

        bookRepository.deleteById(id);
    }

    private Specification<Book> createSpecification(BookSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (criteria.getTitle() != null && !criteria.getTitle().isBlank()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"));
            }

            if (criteria.getAuthorId() != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("author").get("id"), criteria.getAuthorId()));
            }

            if (criteria.getGenreId() != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.isMember(criteria.getGenreId(), root.get("genres")));
            }

            return predicates;
        };
    }

    // Methods to search and create books from external API

    @Transactional
    public BookResponseDTO findOrCreateBookFromApi(String apiId) {
        Optional<Book> existingBook = bookRepository.findByGoogleApiId(apiId);
        if (existingBook.isPresent()) {
            return bookMapper.toDto(existingBook.get());
        }

        BookExternalResponseDTO externalBook = booksClient.findBookById(apiId);
        if (externalBook == null) {
            throw new EntityNotFoundException("Book not found with API ID: " + apiId);
        }

        Book newBook = convertToEntity(externalBook);

        return bookMapper.toDto(bookRepository.save(newBook));
    }

    @Transactional(readOnly = true)
    public List<BookExternalResponseDTO> searchExternalBooksFromApi(ExternalBookSearchCriteria criteria) {
        return booksClient.searchBooks(criteria);
    }

    private Book convertToEntity(BookExternalResponseDTO externalBook) {
        Book book = new Book();
        book.setGoogleApiId(externalBook.getApiId());
        book.setTitle(externalBook.getTitle());
        book.setDescription(externalBook.getDescription());

        // Check if the author is provided and handle "Find or Create"
        // For now, we assume the first author is the main one
        // TODO: Handle multiple authors
        if (!externalBook.getAuthors().isEmpty()) {
            String authorName = externalBook.getAuthors().getFirst();
            Author author = authorRepository.findByName(authorName)
                    .orElseGet(() -> authorRepository.save(new Author(authorName)));
            book.setAuthor(author);
        }

        // Check if categories are provided and handle "Find or Create"
        if (externalBook.getCategories() != null && !externalBook.getCategories().isEmpty()) {
            Set<Genre> genres = externalBook.getCategories().stream()
                    .map(categoryName -> genreRepository.findByName(categoryName)
                            .orElseGet(() -> {
                                return genreRepository.save(new Genre(categoryName));
                            }))
                    .collect(Collectors.toSet());
            book.setGenres(genres);
        }

        return book;
    }
}
