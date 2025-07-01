package com.eduardoxduardo.vlibrary.mapper;

import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserResponseDTO, User>{

    @Override
    public UserResponseDTO toDto(User user) {

        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
            user.getId(),
            user.getUsername()
        );
    }
}
