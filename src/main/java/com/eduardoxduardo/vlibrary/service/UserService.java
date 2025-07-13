package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.LoginRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.create.UserRegisterRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.UserUpdatePasswordRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.ReviewMapper;
import com.eduardoxduardo.vlibrary.model.entities.Review;
import com.eduardoxduardo.vlibrary.model.entities.User;
import com.eduardoxduardo.vlibrary.repository.UserRepository;
import com.eduardoxduardo.vlibrary.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ReviewMapper reviewMapper;

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
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDto(users);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> findAllReviewsByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        List<Review> reviews = user.getLibrary().stream()
                .flatMap(userBook -> userBook.getReviews().stream())
                .toList();

        return reviewMapper.toDto(reviews);
    }

    @Transactional
    public UserResponseDTO updateUserPassword(Long userId, UserUpdatePasswordRequestDTO request, String username) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!user.getUsername().equals(username)) {
            throw new AccessDeniedException("You can only update your own password.");
        }

        if (request.getOldPassword() != null && request.getNewPassword() != null) {
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect.");
            }

            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new IllegalArgumentException("New password and old password are the same.");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long userId, String username) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!user.getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete your own account.");
        }

        userRepository.delete(user);
    }
}
