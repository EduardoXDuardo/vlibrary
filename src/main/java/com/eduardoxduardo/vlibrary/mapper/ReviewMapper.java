package com.eduardoxduardo.vlibrary.mapper;

import com.eduardoxduardo.vlibrary.dto.response.BookSummaryResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.Review;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper implements Mapper<ReviewResponseDTO, Review> {

    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    public ReviewMapper(BookMapper bookMapper, UserMapper userMapper) {
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    @Override
    public ReviewResponseDTO toDto(Review review) {

        if (review == null) {
            return null;
        }

        BookSummaryResponseDTO book = bookMapper.toSummaryDto(review.getUserBook().getBook());
        UserResponseDTO user = userMapper.toDto(review.getUserBook().getUser());

        return new ReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getReviewDate(),
                user,
                book
        );
    }
}
