package com.eduardoxduardo.vlibrary.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "books")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "authors")
public class Author implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "author")
    private Set<Book> books;

    public Author(String name) {
        this.name = name;
    }
}
