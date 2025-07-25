package com.eduardoxduardo.vlibrary.service;

import com.eduardoxduardo.vlibrary.dto.filter.LibrarySearchCriteria;
import com.eduardoxduardo.vlibrary.dto.request.create.ReviewCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.create.UserBookCreateRequestDTO;
import com.eduardoxduardo.vlibrary.dto.request.update.LibraryUpdateReadingStatusRequestDTO;
import com.eduardoxduardo.vlibrary.dto.response.ReviewResponseDTO;
import com.eduardoxduardo.vlibrary.dto.response.UserBookResponseDTO;
import com.eduardoxduardo.vlibrary.mapper.ReviewMapper;
import com.eduardoxduardo.vlibrary.mapper.UserBookMapper;
import com.eduardoxduardo.vlibrary.model.entities.Book;
import com.eduardoxduardo.vlibrary.model.entities.Review;
import com.eduardoxduardo.vlibrary.model.entities.User;
import com.eduardoxduardo.vlibrary.model.entities.UserBook;
import com.eduardoxduardo.vlibrary.model.enums.ReadingStatus;
import com.eduardoxduardo.vlibrary.repository.BookRepository;
import com.eduardoxduardo.vlibrary.repository.UserBookRepository;
import com.eduardoxduardo.vlibrary.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserBookMapper userBookMapper;
    private final ReviewMapper reviewMapper;

    @Transactional
    public UserBookResponseDTO addBookToUserLibrary(UserBookCreateRequestDTO request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + request.getBookId()));

        userBookRepository.findByUserAndBook(user, book)
                .ifPresent(existingUserBook -> {
                    throw new IllegalStateException("This book is already in your library.");
                });

        UserBook newUserBook = new UserBook(user, book, request.getReadingStatus());

        UserBook savedUserBook = userBookRepository.save(newUserBook);
        return userBookMapper.toDto(savedUserBook);
    }

    @Transactional
    public ReviewResponseDTO addReviewToBook(Long userBookID, ReviewCreateRequestDTO request, String username) {
        UserBook userBook = userBookRepository.findById(userBookID)
                .orElseThrow(() -> new EntityNotFoundException("UserBook not found with ID: " + userBookID));

        if(!userBook.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only add a review to your own books.");
        }

        if (userBook.getReadingStatus() == ReadingStatus.WANT_TO_READ) {
            throw new IllegalStateException("You cannot add a review to a book that you want to read.");
        }

        Review newReview = new Review(
                request.getRating(),
                request.getComment(),
                userBook
        );

        userBook.getReviews().add(newReview);

        userBookRepository.save(userBook);

        return reviewMapper.toDto(newReview);
    }

    @Transactional(readOnly = true)
    public Page<UserBookResponseDTO> findInLibrary(LibrarySearchCriteria criteria, int page, int size, String sortBy, String sortDirection, String username) {
        Specification<UserBook> spec = createSpecification(criteria, username);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<UserBook> entries = userBookRepository.findAll(spec, pageRequest);
        return entries.map(userBookMapper::toDto);
    }

    @Transactional
    public UserBookResponseDTO updateReadingStatus(Long userBookId, LibraryUpdateReadingStatusRequestDTO request, String username) {

        UserBook userBook = userBookRepository.findById(userBookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + userBookId));

        if (!userBook.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only update the reading status of your own books.");
        }

        ReadingStatus newReadingStatus = request.getReadingStatus();
        userBook.setReadingStatus(newReadingStatus);

        UserBook updatedUserBook = userBookRepository.save(userBook);
        return userBookMapper.toDto(updatedUserBook);
    }

    @Transactional
    public void deleteBookFromLibrary(Long userBookId, String username) {
        UserBook userBook = userBookRepository.findById(userBookId)
                .orElseThrow(() -> new EntityNotFoundException("UserBook not found with ID: " + userBookId));

        if (!userBook.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete books from your own library.");
        }

        userBookRepository.delete(userBook);
    }

    public Specification<UserBook> createSpecification(LibrarySearchCriteria criteria, String username) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (criteria.getUserId() == null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("user").get("username"), username));
            }
            else {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("user").get("id"), criteria.getUserId()));
            }

            if (criteria.getBookTitle() != null && !criteria.getBookTitle().isBlank()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("book").get("title")), "%" + criteria.getBookTitle().toLowerCase() + "%"));
            }

            if (criteria.getAuthorId() != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("book").get("author").get("id"), criteria.getAuthorId()));
            }

            if (criteria.getGenreId() != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.isMember(criteria.getGenreId(), root.get("book").get("genres")));
            }

            if (criteria.getRating() != null) {
                Join<UserBook, Review> reviewJoin = root.join("reviews", JoinType.LEFT);
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(reviewJoin.get("rating"), criteria.getRating()));
                if (query != null) {
                    query.distinct(true);
                }
            }

            if (criteria.getStatus() != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("readingStatus"), criteria.getStatus()));
            }

            return predicates;
        };
    }
}
