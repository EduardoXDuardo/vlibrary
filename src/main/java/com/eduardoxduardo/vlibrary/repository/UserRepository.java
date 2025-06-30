package com.eduardoxduardo.vlibrary.repository;

import com.eduardoxduardo.vlibrary.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
