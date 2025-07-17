package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.filter.AuthorSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.filter.GenreSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.create.GenreCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.GenreUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.GenreResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.GenreMapper;
import com.eduardoxduardo.vlibrary.model.entities.Book;
import com.eduardoxduardo.vlibrary.model.entities.Genre;
import com.eduardoxduardo.vlibrary.repository.BookRepository;
import com.eduardoxduardo.vlibrary.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final BookRepository bookRepository;

    @Transactional
    public GenreResponseDTO createGenre(GenreCreateRequestDTO request) {
        Genre newGenre = new Genre(request.getName());
        Genre savedGenre = genreRepository.save(newGenre);
        return genreMapper.toDto(savedGenre);
    }

    @Transactional(readOnly = true)
    public Page<GenreResponseDTO> findGenres(GenreSearchCriteria criteria, int page, int size, String sortBy, String sortDirection) {

        Specification<Genre> spec = createSpecification(criteria);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Genre> genres = genreRepository.findAll(spec, pageRequest);

        return genres.map(genreMapper::toDto);
    }

    @Transactional(readOnly = true)
    public GenreResponseDTO getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
        return genreMapper.toDto(genre);
    }

    @Transactional
    public GenreResponseDTO updateGenre(Long genreId, GenreUpdateRequestDTO request) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));

        if (request.getName() != null && !request.getName().isEmpty()) {
            genre.setName(request.getName());
        }

        Genre updatedGenre = genreRepository.save(genre);
        return genreMapper.toDto(updatedGenre);
    }

    @Transactional
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Genre not found with id: " + id);
        }

        List<Book> books = bookRepository.findByGenresId(id);
        for (Book book : books) {
            book.getGenres().removeIf(genre -> genre.getId().equals(id));
        }
        bookRepository.saveAll(books);

        genreRepository.deleteById(id);
    }

    private Specification<Genre> createSpecification(GenreSearchCriteria criteria) {
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
