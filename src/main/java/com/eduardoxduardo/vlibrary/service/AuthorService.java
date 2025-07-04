package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.create.AuthorCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.AuthorUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.AuthorResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.Author;
import com.eduardoxduardo.vlibrary.mapper.AuthorMapper;
import com.eduardoxduardo.vlibrary.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

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

    @Transactional
    public AuthorResponseDTO updateAuthor(Long authorId, AuthorUpdateRequestDTO request) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));

        if (request.getName() != null && !request.getName().isBlank()) {
            author.setName(request.getName());
        }

        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.toDto(updatedAuthor);
    }
}
