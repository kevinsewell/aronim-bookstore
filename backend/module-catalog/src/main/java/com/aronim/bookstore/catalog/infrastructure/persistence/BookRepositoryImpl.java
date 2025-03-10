package com.aronim.bookstore.catalog.infrastructure.persistence;

import com.aronim.bookstore.catalog.domain.exception.BookNotFoundException;
import com.aronim.bookstore.catalog.domain.model.Book;
import com.aronim.bookstore.catalog.domain.model.BookId;
import com.aronim.bookstore.catalog.domain.model.ISBN;
import com.aronim.bookstore.catalog.domain.repository.BookRepository;
import com.aronim.bookstore.catalog.infrastructure.persistence.entity.BookEntity;
import com.aronim.bookstore.catalog.infrastructure.persistence.entity.BookEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookRepositoryImpl implements BookRepository {
    private final BookEntityMapper bookEntityMapper;
    private final BookJpaRepository bookJpaRepository;

    public BookRepositoryImpl(BookEntityMapper bookEntityMapper,
                              BookJpaRepository bookJpaRepository) {
        this.bookEntityMapper = bookEntityMapper;
        this.bookJpaRepository = bookJpaRepository;
    }

    @Override
    public void save(Book book) {
        BookEntity entity = this.bookEntityMapper.fromDomain(book);
        bookJpaRepository.save(entity);
    }

    @Override
    public Optional<Book> findById(BookId id) {
        return bookJpaRepository.findById(id.getValue())
                .map(this.bookEntityMapper::toDomain);
    }

    @Override
    public Book findByIdOrThrow(BookId id) throws BookNotFoundException {
        return this.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public Optional<Book> findByIsbn(ISBN isbn) {
        return bookJpaRepository.findByIsbn(isbn.getValue())
                .map(this.bookEntityMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return bookJpaRepository.findAll().stream()
                .map(this.bookEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(BookId id) {
        bookJpaRepository.deleteById(id.getValue());
    }

    @Override
    public void deleteAll() {
        bookJpaRepository.deleteAllInBatch();
    }
}
