package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.create.UserRegisterRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.UserMapper;
import com.eduardoxduardo.vlibrary.model.entities.User;
import com.eduardoxduardo.vlibrary.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserRegisterRequestDTO registerRequest;
    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        registerRequest = new UserRegisterRequestDTO();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        user = new User("testuser", "test@example.com", "encodedPassword");
        user.setId(1L);

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("testuser");
    }

    @Test
    void registerUser_Success() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDTO);

        // When
        UserResponseDTO result = userService.registerUser(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals(userResponseDTO.getId(), result.getId());
        assertEquals(userResponseDTO.getUsername(), result.getUsername());
        
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    void registerUser_UsernameAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.registerUser(registerRequest);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}