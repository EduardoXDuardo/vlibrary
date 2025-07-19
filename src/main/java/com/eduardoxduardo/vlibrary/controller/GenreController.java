package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.filter.GenreSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.create.GenreCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.GenreUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.GenreResponseDTO;
import com.eduardoxduardo.vlibrary.service.GenreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreResponseDTO> createGenre(@Valid @RequestBody GenreCreateRequestDTO request) {
        return ResponseEntity.status(201).body(genreService.createGenre(request));
    }

    @GetMapping
    public ResponseEntity<Page<GenreResponseDTO>> searchGenres(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        GenreSearchCriteria criteria = new GenreSearchCriteria(name);
        Page<GenreResponseDTO> genres = genreService.findGenres(criteria, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> updateGenre(@PathVariable Long id, @Valid @RequestBody GenreUpdateRequestDTO request) {
        return ResponseEntity.ok(genreService.updateGenre(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
