package com.aronim.bookstore.catalog.application.service;

import com.aronim.bookstore.catalog.application.dto.BookDto;
import com.aronim.bookstore.catalog.application.dto.BookDtoMapper;
import com.aronim.bookstore.catalog.domain.exception.BookNotFoundException;
import com.aronim.bookstore.catalog.domain.model.*;
import com.aronim.bookstore.catalog.domain.repository.BookRepository;
import com.aronim.bookstore.platform.domain.event.DomainEventPublisher;
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

    private final BookDtoMapper bookDtoMapper;
    private final BookRepository bookRepository;
    private final DomainEventPublisher eventPublisher;

    /**
     * Constructs a new BookService with required dependencies.
     *
     * @param bookDtoMapper mapper for converting between domain entities and DTOs
     * @param bookRepository repository for book persistence operations
     * @param eventPublisher publisher for domain events
     */
    public BookServiceImpl(final BookDtoMapper bookDtoMapper,
                           final BookRepository bookRepository,
                           final DomainEventPublisher eventPublisher) {
        this.bookDtoMapper = bookDtoMapper;
        this.bookRepository = bookRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public BookDto createBook(String isbn, String title, String authorFirstName, String authorLastName,
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
    public Optional<BookDto> findBookById(UUID id) {
        return bookRepository.findById(new BookId(id)).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(new ISBN(isbn)).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateBookStock(UUID id, int quantity) throws BookNotFoundException {

        final Book book = bookRepository.findByIdOrThrow(new BookId(id));

        book.updateStock(quantity);
        bookRepository.save(book);

        // Publish all domain events
        book.getDomainEvents().forEach(eventPublisher::publish);
        book.clearDomainEvents();
    }

    @Transactional
    @Override
    public void updateBookPrice(UUID id, BigDecimal price) throws BookNotFoundException {

        final Book book = bookRepository.findByIdOrThrow(new BookId(id));

        book.updatePrice(price);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBook(UUID id) throws BookNotFoundException {

        final Book book = bookRepository.findByIdOrThrow(new BookId(id));

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
    private BookDto mapToDTO(Book book) {
        return this.bookDtoMapper.fromDomain(book);
    }
}
