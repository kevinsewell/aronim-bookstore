package com.aronim.bookstore.application.service;

import com.aronim.bookstore.application.dto.BookDTO;
import com.aronim.bookstore.domain.event.DomainEventPublisher;
import com.aronim.bookstore.domain.model.*;
import com.aronim.bookstore.domain.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing book operations in the bookstore system.
 * <p>
 * This service implements the application layer business logic for book management,
 * coordinating between the domain model, repository, and event publishing mechanisms.
 * It handles operations such as book creation, updates, queries, and inventory management.
 * </p>
 *
 * <p>
 * Key responsibilities:
 * <ul>
 *     <li>Book lifecycle management (creation, updates, deletion)</li>
 *     <li>Inventory and stock management</li>
 *     <li>Domain event publication for significant state changes</li>
 *     <li>Transaction management for data consistency</li>
 *     <li>Data transformation between domain model and DTOs</li>
 * </ul>
 * </p>
 *
 * @see BookRepository
 * @see DomainEventPublisher
 * @see Book
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final DomainEventPublisher eventPublisher;

    /**
     * Constructs a new BookService with required dependencies.
     *
     * @param bookRepository repository for book persistence operations
     * @param eventPublisher publisher for domain events
     */
    public BookServiceImpl(BookRepository bookRepository, DomainEventPublisher eventPublisher) {
        this.bookRepository = bookRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public BookDTO createBook(String isbn, String title, String authorFirstName, String authorLastName,
                              String publisherName, BigDecimal price) {
        Book book = Book.create(
                new ISBN(isbn),
                new Title(title),
                new Author(authorFirstName, authorLastName),
                new Publisher(publisherName)
        );

        book.updatePrice(price);
        bookRepository.save(book);

        // Publish all domain events
        book.getDomainEvents().forEach(eventPublisher::publish);
        book.clearDomainEvents();

        return mapToDTO(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDTO> findBookById(UUID id) {
        return bookRepository.findById(new BookId(id)).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDTO> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(new ISBN(isbn)).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateBookStock(UUID id, int quantity) {
        bookRepository
                .findById(new BookId(id))
                .ifPresent(book -> {
                    book.updateStock(quantity);
                    bookRepository.save(book);

                    // Publish all domain events
                    book.getDomainEvents().forEach(eventPublisher::publish);
                    book.clearDomainEvents();
                });
    }

    @Transactional
    @Override
    public void updateBookPrice(UUID id, BigDecimal price) {
        bookRepository
                .findById(new BookId(id))
                .ifPresent(book -> {
                    book.updatePrice(price);
                    bookRepository.save(book);
                });
    }

    @Transactional
    @Override
    public void deleteBook(UUID id) {
        bookRepository.delete(new BookId(id));
    }

    @Override
    public void deleteAllBooks() {
        bookRepository.deleteAll();
    }

    /**
     * Maps a Book domain entity to its DTO representation.
     *
     * @param book the domain entity to map
     * @return DTO representing the book
     */
    private BookDTO mapToDTO(Book book) {
        return new BookDTO(
                book.getId().getValue(),
                book.getIsbn().getValue(),
                book.getTitle().getValue(),
                book.getAuthor().getFullName(),
                book.getPublisher().getName(),
                book.getPublishDate(),
                book.getPrice(),
                book.getStockQuantity(),
                book.getStatus().name()
        );
    }
}
