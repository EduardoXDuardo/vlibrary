package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.request.GenreCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.GenreResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.GenreMapper;
import com.eduardoxduardo.vlibrary.model.entities.Genre;
import com.eduardoxduardo.vlibrary.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<GenreResponseDTO> findAllGenres() {
    }

    @Transactional(readOnly = true)
    public GenreResponseDTO findGenreById(Long id) {
    }
}
