package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.filter.ReviewSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.update.ReviewUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.ReviewMapper;
import com.eduardoxduardo.vlibrary.model.entities.Review;
import com.eduardoxduardo.vlibrary.repository.ReviewRepository;
import com.eduardoxduardo.vlibrary.repository.UserBookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserBookRepository userBookRepository;

    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(reviewMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDTO> findReviews(ReviewSearchCriteria criteria, int page, int size, String sortBy, String sortDirection, String username) {
        Specification<Review> spec = createSpecification(criteria, username);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Review> reviews = reviewRepository.findAll(spec, pageRequest);
        return reviews.map(reviewMapper::toDto);
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

    private Specification<Review> createSpecification(ReviewSearchCriteria criteria, String username) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (criteria.getUserId() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("userBook").get("user").get("id"), criteria.getUserId()));
            }
            else {
                // If no userId is provided, we assume the search is for the current user's reviews
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("userBook").get("user").get("username"), username));
            }

            if (criteria.getBookId() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("userBook").get("book").get("id"), criteria.getBookId()));
            }

            if (criteria.getCommentContains() != null && !criteria.getCommentContains().isBlank()) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")), "%" + criteria.getCommentContains().toLowerCase() + "%"));
            }

            if (criteria.getRating() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("rating"), criteria.getRating()));
            }

            return predicates;
        };
    }
}
