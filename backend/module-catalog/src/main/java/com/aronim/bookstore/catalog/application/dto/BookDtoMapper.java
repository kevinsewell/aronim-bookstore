package com.aronim.bookstore.catalog.application.dto;

import com.aronim.bookstore.catalog.domain.model.Book;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookDtoMapper {

    private final ModelMapper modelMapper;

    public BookDtoMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BookDto fromDomain(final Book book) {
        return this.modelMapper.map(book, BookDto.class);
    }

    public Book toDomain(final BookDto bookDto) {
        return this.modelMapper.map(bookDto, Book.class);
    }
}
