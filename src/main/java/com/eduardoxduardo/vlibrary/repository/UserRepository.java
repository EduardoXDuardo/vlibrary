package com.eduardoxduardo.vlibrary.repository;

import com.eduardoxduardo.vlibrary.model.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@NotBlank(message = "Username is required") String username);
}
