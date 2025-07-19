package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.filter.BookSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.filter.LibrarySearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.create.ReviewCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.create.UserBookCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.LibraryUpdateReadingStatusRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.BookResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserBookResponseDTO;
import com.eduardoxduardo.vlibrary.model.enums.ReadingStatus;
import com.eduardoxduardo.vlibrary.service.LibraryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/library")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping
    public ResponseEntity<UserBookResponseDTO> addBook(
            @Valid @RequestBody UserBookCreateRequestDTO request,
            Principal principal
    ){
        return ResponseEntity.status(201).body(libraryService.addBookToUserLibrary(request, principal.getName()));
    }

    @PostMapping("/{userBookId}/reviews")
    public ResponseEntity<ReviewResponseDTO> addReview(
            @PathVariable Long userBookId,
            @Valid @RequestBody ReviewCreateRequestDTO request,
            Principal principal
    ) {
        return ResponseEntity.status(201).body(libraryService.addReviewToBook(userBookId, request, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<Page<UserBookResponseDTO>> searchLibrary(
            @RequestParam(required = false ) Long userId,
            @RequestParam(required = false) String bookTitle,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) ReadingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            Principal principal
    ) {
        LibrarySearchCriteria criteria = new LibrarySearchCriteria(userId, bookTitle, authorId, genreId, rating, status);
        Page<UserBookResponseDTO> entries = libraryService.searchLibrary(criteria, page, size, sortBy, sortDirection, principal.getName());
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{userBookId}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUserBookId(@PathVariable Long userBookId) {
        return ResponseEntity.ok(libraryService.findAllReviewsByUserBookId(userBookId));
    }

    @PatchMapping("/{userBookId}/reading-status")
    public ResponseEntity<UserBookResponseDTO> updateReadingStatus(
            @PathVariable Long userBookId,
            @Valid @RequestBody LibraryUpdateReadingStatusRequestDTO request,
            Principal principal
    ){
        return ResponseEntity.ok(libraryService.updateReadingStatus(userBookId, request, principal.getName()));
    }

    @DeleteMapping("/{userBookId}")
    public ResponseEntity<Void> deleteBookFromLibrary(
            @PathVariable Long userBookId,
            Principal principal
    ){
        libraryService.deleteBookFromLibrary(userBookId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
