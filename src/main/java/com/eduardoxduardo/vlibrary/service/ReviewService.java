package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.ReviewMapper;
import com.eduardoxduardo.vlibrary.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(reviewMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
    }
}
