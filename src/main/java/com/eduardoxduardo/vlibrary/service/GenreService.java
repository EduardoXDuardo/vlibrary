package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.create.GenreCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.GenreUpdateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.GenreResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.GenreMapper;
import com.eduardoxduardo.vlibrary.model.entities.Genre;
import com.eduardoxduardo.vlibrary.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional
    public GenreResponseDTO createGenre(GenreCreateRequestDTO request) {
        Genre newGenre = new Genre(request.getName());
        Genre savedGenre = genreRepository.save(newGenre);
        return genreMapper.toDto(savedGenre);
    }

    @Transactional(readOnly = true)
    public List<GenreResponseDTO> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genreMapper.toDto(genres);
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
}
