package com.aronim.bookstore.catalog.infrastructure.persistence;

import com.aronim.bookstore.catalog.infrastructure.persistence.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link BookEntity} instances.
 * <p>
 * This repository provides CRUD operations for books stored in the database.
 * It extends JpaRepository which provides standard data access operations.
 * </p>
 */
@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, UUID> {

    /**
     * Finds a book entity by its ISBN (International Standard Book Number).
     *
     * @param isbn the ISBN of the book to search for
     * @return an Optional containing the found BookEntity or an empty Optional if no book with the given ISBN exists
     */
    Optional<BookEntity> findByIsbn(String isbn);
}
