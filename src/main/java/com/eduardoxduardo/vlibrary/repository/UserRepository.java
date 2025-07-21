package com.eduardoxduardo.vlibrary.repository;

import com.eduardoxduardo.vlibrary.model.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(@NotBlank(message = "Username is required") String username);
    Optional<User> findByEmail(@NotBlank(message = "Email is required") String email);
}
