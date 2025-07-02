package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.update.ReviewUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.ReviewMapper;
import com.eduardoxduardo.vlibrary.model.entities.Review;
import com.eduardoxduardo.vlibrary.model.entities.UserBook;
import com.eduardoxduardo.vlibrary.repository.ReviewRepository;
import com.eduardoxduardo.vlibrary.repository.UserBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserBookRepository userBookRepository;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper, UserBookRepository userBookRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.userBookRepository = userBookRepository;
    }

    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(reviewMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> findAllReviewsByUserBookId(Long userBookId) {
        UserBook userBook = userBookRepository.findById(userBookId)
                .orElseThrow(() -> new EntityNotFoundException("UserBook not found with ID: " + userBookId));

        return reviewMapper.toDto(userBook.getReviews());
    }

    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, ReviewUpdateRequestDTO request, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));

        if (!review.getUserBook().getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied: You do not own this review.");
        }

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }

        if (request.getComment() != null && !request.getComment().isBlank()) {
            review.setComment(request.getComment());
        }

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDto(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));

        if (!review.getUserBook().getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied: You do not own this review.");
        }

        review.getUserBook().getReviews().remove(review);
        review.setUserBook(null);

        reviewRepository.delete(review);
    }
}
