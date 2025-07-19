package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.filter.BookSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.filter.UserSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.LoginRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.create.UserRegisterRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.UserUpdatePasswordRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.ReviewMapper;
import com.eduardoxduardo.vlibrary.model.entities.Book;
import com.eduardoxduardo.vlibrary.model.entities.Review;
import com.eduardoxduardo.vlibrary.model.entities.User;
import com.eduardoxduardo.vlibrary.repository.UserRepository;
import com.eduardoxduardo.vlibrary.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findUsers(UserSearchCriteria criteria, int page, int size, String sortBy, String sortDirection) {
        Specification<User> spec = createSpecification(criteria);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<User> users = userRepository.findAll(spec, pageRequest);
        return users.map(userMapper::toDto);
    }

    public UserResponseDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
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

    private Specification<User> createSpecification(UserSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (criteria.getEmail() != null && !criteria.getEmail().isBlank()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + criteria.getEmail().toLowerCase() + "%"));
            }

            if (criteria.getUsername() != null && !criteria.getUsername().isBlank()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + criteria.getUsername().toLowerCase() + "%"));
            }

            return predicates;
        };
    }
}
