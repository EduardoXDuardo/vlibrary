package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.UserRegisterRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.User;
import com.eduardoxduardo.vlibrary.repository.UserRepository;
import com.eduardoxduardo.vlibrary.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponseDTO registerUser(UserRegisterRequestDTO request){
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new IllegalStateException("Username already exists");
        });

        User newUser = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(newUser);

        return userMapper.toDto(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username) {
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> findAllReviewsByUser(String username) {
    }
}
