package com.eduardoxduardo.vlibrary.mapper;

import com.eduardoxduardo.vlibrary.model.entities.Review;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper implements Mapper<ReviewResponseDTO, Review> {

    @Override
    public ReviewResponseDTO toDto(Review review) {

        if (review == null) {
            return null;
        }

        return new ReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getReviewDate()
        );
    }
}
