package com.eduardoxduardo.vlibrary.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "userBook")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "reviews")
public class Review implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double rating;
    private String comment;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @ManyToOne
    @JoinColumn(name = "user_book_id", nullable = false)
    private UserBook userBook;

    public Review(double rating, String comment, UserBook userBook) {
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDate.now();
        this.userBook = userBook;
    }
}
