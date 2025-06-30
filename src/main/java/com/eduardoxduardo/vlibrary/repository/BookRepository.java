package com.eduardoxduardo.vlibrary.repository;

import com.eduardoxduardo.vlibrary.model.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
