package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.filter.ReviewSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.update.ReviewUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/reviews")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ReviewResponseDTO>> searchLibrary(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) String commentContains,
            @RequestParam(required = false) Double rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            Principal principal
    ) {
        ReviewSearchCriteria criteria = new ReviewSearchCriteria(userId, bookId, commentContains, rating);
        Page<ReviewResponseDTO> reviews = reviewService.searchReviews(criteria, page, size, sortBy, sortDirection, principal.getName());
        return ResponseEntity.ok(reviews);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview (
            @PathVariable Long id,
            @RequestBody @Valid ReviewUpdateRequestDTO request,
            Principal principal
    ) {
        return ResponseEntity.ok(reviewService.updateReview(id, request, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview (
            @PathVariable Long id,
            Principal principal
    ) {

        reviewService.deleteReview(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
