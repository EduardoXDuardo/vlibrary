package com.eduardoxduardo.vlibrary.model.entities;

import com.eduardoxduardo.vlibrary.model.enums.ReadingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user", "book", "review"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "user_books")
public class UserBook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingStatus readingStatus;

    @OneToMany(mappedBy = "userBook", cascade = CascadeType.ALL)
    private List<Review> reviews;

    public UserBook(User user, Book book, ReadingStatus readingStatus) {
        this.user = user;
        this.book = book;
        this.readingStatus = readingStatus;
    }
}