package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.filter.BookSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.filter.ExternalBookSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.create.BookCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.BookUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookExternalResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookResponseDTO;
import com.eduardoxduardo.vlibrary.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // Endpoint to create a new book manually if it does not exist in the book api
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookCreateRequestDTO request) {
        return ResponseEntity.status(201).body(bookService.createBook(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long genreId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        BookSearchCriteria criteria = new BookSearchCriteria(title, authorId, genreId);
        Page<BookResponseDTO> books = bookService.findBooks(criteria, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(books);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateRequestDTO request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/external/{apiId}")
    public ResponseEntity<BookResponseDTO> importBookFromApi(@PathVariable String apiId) {
        return ResponseEntity.status(201).body(bookService.findOrCreateBookFromApi(apiId));
    }

    @GetMapping("/external")
    public ResponseEntity<List<BookExternalResponseDTO>> searchExternalBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String language
    ) {
        ExternalBookSearchCriteria criteria = new ExternalBookSearchCriteria(title, author, publisher, category, isbn, language);
        List<BookExternalResponseDTO> books = bookService.searchExternalBooksFromApi(criteria);
        return ResponseEntity.ok(books);
    }
}
