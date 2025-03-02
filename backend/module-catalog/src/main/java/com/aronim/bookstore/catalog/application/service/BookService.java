package com.aronim.bookstore.catalog.application.service;

import com.aronim.bookstore.catalog.application.dto.BookDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing book operations in the bookstore system.
 * <p>
 * This interface defines the contract for the application service layer responsible for
 * handling book-related business operations. It serves as a facade between the presentation
 * layer and the domain model, coordinating multiple domain objects and their operations.
 * </p>
 *
 * <p>
 * The service layer is responsible for:
 * <ul>
 *     <li>Coordinating operations between different domain objects</li>
 *     <li>Managing transactions and ensuring data consistency</li>
 *     <li>Publishing domain events</li>
 *     <li>Enforcing application-level business rules</li>
 *     <li>Providing a clean API for the presentation layer</li>
 * </ul>
 * </p>
 *
 * <p>
 * This service follows the principle of Interface Segregation and should be
 * implemented by concrete classes that provide the actual business logic.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @RestController
 * public class BookController {
 *     private final BookService bookService;
 *
 *     public BookController(BookService bookService) {
 *         this.bookService = bookService;
 *     }
 *
 *     // Controller methods using bookService...
 * }
 * }
 * </pre>
 * </p>
 *
 * @see com.aronim.bookstore.domain.model.Book
 * @see com.aronim.bookstore.domain.repository.BookRepository
 * @see com.aronim.bookstore.domain.event.DomainEventPublisher
 */
public interface BookService {
    /**
     * Creates a new book in the system.
     * <p>
     * This method:
     * <ul>
     *     <li>Creates a new Book aggregate with the provided details</li>
     *     <li>Sets the initial price</li>
     *     <li>Persists the book to the repository</li>
     *     <li>Publishes relevant domain events</li>
     * </ul>
     * </p>
     *
     * @param isbn            ISBN of the book
     * @param title           title of the book
     * @param authorFirstName author's first name
     * @param authorLastName  author's last name
     * @param publisherName   name of the publisher
     * @param price           initial price of the book
     * @return DTO representing the created book
     * @throws IllegalArgumentException if any parameter is invalid
     */
    @Transactional
    BookDTO createBook(String isbn, String title, String authorFirstName, String authorLastName,
                       String publisherName, BigDecimal price);

    /**
     * Finds a book by its unique identifier.
     *
     * @param id unique identifier of the book
     * @return Optional containing the book DTO if found, empty otherwise
     */
    @Transactional(readOnly = true)
    Optional<BookDTO> findBookById(UUID id);

    /**
     * Finds a book by its ISBN.
     *
     * @param isbn ISBN of the book to find
     * @return Optional containing the book DTO if found, empty otherwise
     */
    @Transactional(readOnly = true)
    Optional<BookDTO> findBookByIsbn(String isbn);

    /**
     * Retrieves all books in the system.
     * <p>
     * Note: In a production environment, this method should be paginated
     * to handle large datasets efficiently.
     * </p>
     *
     * @return List of all books as DTOs, never null but may be empty
     */
    @Transactional(readOnly = true)
    List<BookDTO> findAllBooks();

    /**
     * Updates the stock quantity of a book.
     * <p>
     * This method:
     * <ul>
     *     <li>Retrieves the book from the repository</li>
     *     <li>Updates the stock quantity</li>
     *     <li>Persists the changes</li>
     *     <li>Publishes a BookStockUpdatedEvent</li>
     * </ul>
     * </p>
     *
     * @param id       unique identifier of the book
     * @param quantity amount to adjust the stock by (positive or negative)
     * @throws IllegalStateException if attempting to remove more stock than available
     */
    @Transactional
    void updateBookStock(UUID id, int quantity);

    /**
     * Updates the price of a book.
     *
     * @param id    unique identifier of the book
     * @param price new price for the book
     * @throws IllegalArgumentException if the price is not positive
     */
    @Transactional
    void updateBookPrice(UUID id, BigDecimal price);

    /**
     * Deletes a book from the system.
     *
     * @param id unique identifier of the book to delete
     */
    @Transactional
    void deleteBook(UUID id);

    /**
     * Deletes all books from the system.
     * <p>
     * This is a potentially destructive operation that removes all books
     * from the system. It should be used with caution and proper authorization.
     * </p>
     */
    @Transactional
    void deleteAllBooks();
}
