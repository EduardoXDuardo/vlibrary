package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.AuthorCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.AuthorResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.Author;
import com.eduardoxduardo.vlibrary.mapper.AuthorMapper;
import com.eduardoxduardo.vlibrary.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Transactional
    public AuthorResponseDTO createAuthor(AuthorCreateRequestDTO request) {
        Author newAuthor = new Author(request.getName());
        Author savedAuthor = authorRepository.save(newAuthor);
        return authorMapper.toDto(savedAuthor);
    }

    @Transactional(readOnly = true)
    public List<AuthorResponseDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.toDto(authors);
    }

    @Transactional(readOnly = true)
    public AuthorResponseDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return authorMapper.toDto(author);
    }
}
