package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.filter.AuthorSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.filter.BookSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.create.AuthorCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.AuthorUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.AuthorResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.Book;
import com.eduardoxduardo.vlibrary.service.AuthorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@Valid @RequestBody AuthorCreateRequestDTO request) {
        return ResponseEntity.status(201).body(authorService.createAuthor(request));
    }

    @GetMapping
    public ResponseEntity<Page<AuthorResponseDTO>> searchAuthors(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        AuthorSearchCriteria criteria = new AuthorSearchCriteria(name);
        Page<AuthorResponseDTO> authors = authorService.findAuthors(criteria, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorUpdateRequestDTO request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
