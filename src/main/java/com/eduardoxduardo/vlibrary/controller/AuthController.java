package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.request.LoginRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.create.UserRegisterRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.LoginResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.service.AuthService;
import com.eduardoxduardo.vlibrary.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO request) {
        return ResponseEntity.status(201).body(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
