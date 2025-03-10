package com.aronim.bookstore.catalog.infrastructure.persistence.entity;

import com.aronim.bookstore.catalog.domain.model.Book;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookEntityMapper {

    private final ModelMapper modelMapper;

    public BookEntityMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BookEntity fromDomain(final Book book) {
        return this.modelMapper.map(book, BookEntity.class);
    }

    public Book toDomain(final BookEntity bookEntity) {
        return this.modelMapper.map(bookEntity, Book.class);
    }
}
