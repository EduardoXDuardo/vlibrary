package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.GenreCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.GenreResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.GenreMapper;
import com.eduardoxduardo.vlibrary.model.entities.Genre;
import com.eduardoxduardo.vlibrary.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Transactional
    public GenreResponseDTO createGenre(GenreCreateRequestDTO request) {
        Genre newGenre = new Genre(request.getName());
        Genre savedGenre = genreRepository.save(newGenre);
        return genreMapper.toDto(savedGenre);
    }

    @Transactional(readOnly = true)
    public List<GenreResponseDTO> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GenreResponseDTO getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
        return genreMapper.toDto(genre);
    }
}
