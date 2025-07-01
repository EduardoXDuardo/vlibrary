package com.eduardoxduardo.vlibrary.mapper;

import com.eduardoxduardo.vlibrary.dto.response.BookResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserBookResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.UserBook;
import org.springframework.stereotype.Component;

@Component
public class UserBookMapper implements Mapper<UserBookResponseDTO, UserBook> {

    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserBookMapper(UserMapper userMapper, BookMapper bookMapper) {
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    public UserBookResponseDTO toDto(UserBook userBook) {

        if (userBook == null) {
            return null;
        }

        UserResponseDTO userDTO = userMapper.toDto(userBook.getUser());
        BookResponseDTO bookDTO = bookMapper.toDto(userBook.getBook());

        return new UserBookResponseDTO(
                userBook.getId(),
                userBook.getReadingStatus(),
                bookDTO,
                userDTO
        );
    }
}
