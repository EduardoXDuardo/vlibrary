package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.request.create.BookCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.BookUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookResponseDTO;
import com.eduardoxduardo.vlibrary.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookCreateRequestDTO request) {
        return ResponseEntity.status(201).body(bookService.createBook(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // This endpoint allows searching for books by title, author ID, or genre ID
    // Only one of these parameters should be provided at a time for now.
    // TO DO: Needs to be improved in the future to allow multiple filters.
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> findBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long genreId
    ) {

        if (title != null) {
            return ResponseEntity.ok(bookService.findBooksByTitle(title));
        }
        if (authorId != null) {
            return ResponseEntity.ok(bookService.findAllBooksByAuthorId(authorId));
        }
        if (genreId != null) {
            return ResponseEntity.ok(bookService.findAllBooksByGenreId(genreId));
        }

        return ResponseEntity.ok(bookService.getAllBooks());
    }
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateRequestDTO request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }
}
