package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.request.update.UserUpdatePasswordRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(userService.getUserByUsername(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<UserResponseDTO> findUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {

        if (username != null) {
            return ResponseEntity.ok(userService.getUserByUsername(username));
        }
        if (email != null) {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UserResponseDTO> updateUserPassword(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdatePasswordRequestDTO request,
            Principal principal
    ){

        return ResponseEntity.ok(userService.updateUserPassword(id, request, principal.getName()));
    }
}
