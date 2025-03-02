package com.aronim.bookstore.catalog.domain.repository;

import com.aronim.bookstore.catalog.domain.model.Book;
import com.aronim.bookstore.catalog.domain.model.BookId;
import com.aronim.bookstore.catalog.domain.model.ISBN;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Book} aggregates in the system.
 * <p>
 * This repository provides the necessary operations to persist and retrieve Book aggregates,
 * following Domain-Driven Design principles. It acts as a collection-like interface for
 * domain objects and serves as an abstraction over the actual storage mechanism.
 * </p>
 *
 * <p>
 * The repository handles the following responsibilities:
 * <ul>
 *     <li>Persisting Book aggregates</li>
 *     <li>Retrieving Books by various identifiers</li>
 *     <li>Managing the lifecycle of Book aggregates</li>
 *     <li>Ensuring consistency of the aggregate root</li>
 * </ul>
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * public class BookService {
 *     private final BookRepository bookRepository;
 *
 *     public Book findBook(BookId id) {
 *         return bookRepository.findById(id)
 *             .orElseThrow(() -> new BookNotFoundException(id));
 *     }
 * }
 * }
 * </pre>
 * </p>
 *
 * @see Book
 * @see BookId
 * @see ISBN
 */
public interface BookRepository {

    /**
     * Persists a Book aggregate to the repository.
     * <p>
     * If the book already exists, it will be updated. If it doesn't exist,
     * a new record will be created. This method ensures all changes to the
     * aggregate are persisted atomically.
     * </p>
     *
     * @param book the Book aggregate to save. Must not be null.
     * @throws IllegalArgumentException if the book parameter is null
     * @throws RuntimeException         if there is an error during persistence
     */
    void save(Book book);

    /**
     * Retrieves a Book aggregate by its unique identifier.
     * <p>
     * This method returns an Optional which will be empty if no Book
     * is found with the given ID.
     * </p>
     *
     * @param id the unique identifier of the book to find. Must not be null.
     * @return an Optional containing the found Book, or empty if not found
     * @throws IllegalArgumentException if the id parameter is null
     */
    Optional<Book> findById(BookId id);

    /**
     * Retrieves a Book aggregate by its ISBN.
     * <p>
     * ISBN (International Standard Book Number) is a unique identifier for books
     * that is standardized internationally. This method returns an Optional which
     * will be empty if no Book is found with the given ISBN.
     * </p>
     *
     * @param isbn the ISBN of the book to find. Must not be null.
     * @return an Optional containing the found Book, or empty if not found
     * @throws IllegalArgumentException if the isbn parameter is null
     */
    Optional<Book> findByIsbn(ISBN isbn);

    /**
     * Retrieves all Book aggregates stored in the repository.
     * <p>
     * This method returns a List containing all Books in the repository.
     * If no books exist, an empty List will be returned.
     * </p>
     *
     * <p>
     * Note: In a production environment with large datasets, this method
     * should be used with caution and might need pagination.
     * </p>
     *
     * @return a List containing all Books, never null but may be empty
     */
    List<Book> findAll();

    /**
     * Removes a Book aggregate from the repository.
     * <p>
     * This operation permanently deletes the Book aggregate identified by
     * the given ID. If no Book exists with the specified ID, this operation
     * will complete silently.
     * </p>
     *
     * @param id the unique identifier of the book to delete. Must not be null.
     * @throws IllegalArgumentException if the id parameter is null
     */
    void delete(BookId id);

    /**
     * Removes all Book aggregates from the repository.
     * <p>
     * This operation permanently deletes all Book aggregates stored in the repository.
     * Use with extreme caution as this operation cannot be undone and will result in
     * the loss of all book data.
     * </p>
     *
     * <p>
     * This method is typically used for:
     * <ul>
     *     <li>Testing environments where a clean state is required</li>
     *     <li>System resets or migrations where all data needs to be purged</li>
     *     <li>Administrative operations that require clearing all book entries</li>
     * </ul>
     * </p>
     *
     * @throws RuntimeException if there is an error during the deletion process
     */
    void deleteAll();
}
