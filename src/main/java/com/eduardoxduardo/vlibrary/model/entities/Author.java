package com.eduardoxduardo.vlibrary.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    private Long id;
    private String name;

    @OneToMany(mappedBy = "author")
    private Set<Book> books;

    public Author(String name) {
        this.name = name;
    }
}
