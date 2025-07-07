package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.request.update.ReviewUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
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
