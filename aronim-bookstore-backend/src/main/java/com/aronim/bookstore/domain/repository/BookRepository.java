package com.aronim.bookstore.domain.repository;

import com.aronim.bookstore.domain.model.Book;
import com.aronim.bookstore.domain.model.BookId;
import com.aronim.bookstore.domain.model.ISBN;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void save(Book book);

    Optional<Book> findById(BookId id);

    Optional<Book> findByIsbn(ISBN isbn);

    List<Book> findAll();

    void delete(BookId id);
}
