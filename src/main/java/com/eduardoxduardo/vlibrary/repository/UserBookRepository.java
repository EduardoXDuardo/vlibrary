package com.eduardoxduardo.vlibrary.repository;

import com.eduardoxduardo.vlibrary.model.entities.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {
}
