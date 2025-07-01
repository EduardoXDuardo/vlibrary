package com.eduardoxduardo.vlibrary.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface Mapper<D, E> {
    D toDto(E entity);

    default Set<D> toDto(Set<E> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(entity -> toDto(entity))
                .collect(Collectors.toSet());
    }

    default List<D> toDto(List<E> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(entity -> toDto(entity))
                .collect(Collectors.toList());
    }

}
