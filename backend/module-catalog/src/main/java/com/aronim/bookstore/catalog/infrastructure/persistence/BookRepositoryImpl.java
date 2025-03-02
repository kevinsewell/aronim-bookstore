package com.aronim.bookstore.catalog.infrastructure.persistence;

import com.aronim.bookstore.catalog.domain.model.Book;
import com.aronim.bookstore.catalog.domain.model.BookId;
import com.aronim.bookstore.catalog.domain.model.ISBN;
import com.aronim.bookstore.catalog.domain.repository.BookRepository;
import com.aronim.bookstore.catalog.infrastructure.persistence.entity.BookEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookRepositoryImpl implements BookRepository {
    private final BookJpaRepository bookJpaRepository;

    public BookRepositoryImpl(BookJpaRepository bookJpaRepository) {
        this.bookJpaRepository = bookJpaRepository;
    }

    @Override
    public void save(Book book) {
        BookEntity entity = BookEntity.fromDomain(book);
        bookJpaRepository.save(entity);
    }

    @Override
    public Optional<Book> findById(BookId id) {
        return bookJpaRepository.findById(id.getValue())
                .map(BookEntity::toDomain);
    }

    @Override
    public Optional<Book> findByIsbn(ISBN isbn) {
        return bookJpaRepository.findByIsbn(isbn.getValue())
                .map(BookEntity::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return bookJpaRepository.findAll().stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(BookId id) {
        bookJpaRepository.deleteById(id.getValue());
    }

    @Override
    public void deleteAll() {
        bookJpaRepository.deleteAll();
    }
}
