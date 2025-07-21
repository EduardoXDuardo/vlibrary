package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.filter.AuthorSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.create.AuthorCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.AuthorUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.AuthorResponseDTO;
import com.eduardoxduardo.vlibrary.model.entities.Author;
import com.eduardoxduardo.vlibrary.mapper.AuthorMapper;
import com.eduardoxduardo.vlibrary.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

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
    public Page<AuthorResponseDTO> findAuthors(AuthorSearchCriteria criteria, int page, int size, String sortBy, String sortDirection) {

        Specification<Author> spec = createSpecification(criteria);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Author> authors = authorRepository.findAll(spec, pageRequest);

        return authors.map(authorMapper::toDto);
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

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Author not found with id: " + id);
        }

        if (authorRepository.existsByBooks_AuthorId(id)) {
            throw new IllegalStateException("Cannot delete author with associated books.");
        }

        authorRepository.deleteById(id);
    }

    private Specification<Author> createSpecification(AuthorSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (criteria.getName() != null && !criteria.getName().isBlank()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }

            return predicates;
        };
    }
}
