package com.aronim.bookstore.application.service;

import com.aronim.bookstore.application.dto.BookDTO;
import com.aronim.bookstore.domain.model.*;
import com.aronim.bookstore.domain.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
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
        
        return mapToDTO(book);
    }

    @Transactional(readOnly = true)
    public Optional<BookDTO> findBookById(UUID id) {
        return bookRepository.findById(new BookId(id)).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Optional<BookDTO> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(new ISBN(isbn)).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateBookStock(UUID id, int quantity) {
        bookRepository.findById(new BookId(id)).ifPresent(book -> {
            book.updateStock(quantity);
            bookRepository.save(book);
        });
    }

    @Transactional
    public void updateBookPrice(UUID id, BigDecimal price) {
        bookRepository.findById(new BookId(id)).ifPresent(book -> {
            book.updatePrice(price);
            bookRepository.save(book);
        });
    }

    @Transactional
    public void deleteBook(UUID id) {
        bookRepository.delete(new BookId(id));
    }

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
