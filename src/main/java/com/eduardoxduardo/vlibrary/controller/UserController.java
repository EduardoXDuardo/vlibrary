package com.eduardoxduardo.vlibrary.controller;

import com.eduardoxduardo.vlibrary.dto.filter.UserSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.update.UserUpdatePasswordRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserResponseDTO;
import com.eduardoxduardo.vlibrary.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
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

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        UserSearchCriteria criteria = new UserSearchCriteria(username, email);
        Page<UserResponseDTO> users = userService.findUsers(criteria, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UserResponseDTO> updateUserPassword(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdatePasswordRequestDTO request,
            Principal principal
    ){

        return ResponseEntity.ok(userService.updateUserPassword(id, request, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Principal principal) {
        userService.deleteUser(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
