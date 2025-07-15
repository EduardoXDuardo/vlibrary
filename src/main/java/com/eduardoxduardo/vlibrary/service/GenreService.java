package com.eduardoxduardo.vlibrary.service;

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
}
