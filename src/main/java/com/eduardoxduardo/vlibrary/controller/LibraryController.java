package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.request.create.ReviewCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.create.UserBookCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.LibraryUpdateReadingStatusRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserBookResponseDTO;
import com.eduardoxduardo.vlibrary.service.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/library")
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
    public ResponseEntity<List<UserBookResponseDTO>> getUserLibrary(Principal principal){
        return ResponseEntity.ok(libraryService.findAllBooksByUser(principal.getName()));
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
