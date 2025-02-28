package com.aronim.bookstore.presentation.controller;

import com.aronim.bookstore.application.dto.BookDTO;
import com.aronim.bookstore.application.service.BookService;
import com.aronim.bookstore.presentation.request.CreateBookRequest;
import com.aronim.bookstore.presentation.request.UpdateBookPriceRequest;
import com.aronim.bookstore.presentation.request.UpdateBookStockRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody CreateBookRequest request) {
        BookDTO book = bookService.createBook(
            request.getIsbn(),
            request.getTitle(),
            request.getAuthorFirstName(),
            request.getAuthorLastName(),
            request.getPublisher(),
            request.getPrice()
        );
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable UUID id) {
        return bookService.findBookById(id)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findBookByIsbn(isbn)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.findAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateBookStock(@PathVariable UUID id, 
                                               @Valid @RequestBody UpdateBookStockRequest request) {
        bookService.updateBookStock(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<Void> updateBookPrice(@PathVariable UUID id, 
                                              @Valid @RequestBody UpdateBookPriceRequest request) {
        bookService.updateBookPrice(id, request.getPrice());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
