package com.eduardoxduardo.vlibrary.mapper;

import com.eduardoxduardo.vlibrary.dto.response.BookResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserBookResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.UserBook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserBookMapper implements Mapper<UserBookResponseDTO, UserBook> {

    private final UserMapper userMapper;
    private final BookMapper bookMapper;
    private final ReviewMapper reviewMapper;

    public UserBookMapper(UserMapper userMapper, BookMapper bookMapper, ReviewMapper reviewMapper) {
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public UserBookResponseDTO toDto(UserBook userBook) {

        if (userBook == null) {
            return null;
        }

        UserResponseDTO userDTO = userMapper.toDto(userBook.getUser());
        BookResponseDTO bookDTO = bookMapper.toDto(userBook.getBook());

        List<ReviewResponseDTO> reviews = userBook.getReviews().stream()
                .map(reviewMapper::toDto)
                .toList();

        return new UserBookResponseDTO(
                userBook.getId(),
                userBook.getReadingStatus(),
                bookDTO,
                userDTO,
                reviews
        );
    }
}
