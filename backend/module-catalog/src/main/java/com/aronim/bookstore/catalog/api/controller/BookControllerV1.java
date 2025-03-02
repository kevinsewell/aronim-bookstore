package com.aronim.bookstore.catalog.api.controller;

import com.aronim.bookstore.catalog.application.dto.BookDTO;
import com.aronim.bookstore.catalog.application.service.BookService;
import com.aronim.bookstore.catalog.api.request.CreateBookRequest;
import com.aronim.bookstore.catalog.api.request.UpdateBookPriceRequest;
import com.aronim.bookstore.catalog.api.request.UpdateBookStockRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for handling book-related operations.
 * <p>
 * This controller provides endpoints for creating, retrieving, updating, and deleting books
 * in the bookstore system. It handles HTTP requests and delegates business logic to the
 * {@link BookService}.
 * </p>
 * <p>
 * Available operations:
 * <ul>
 *   <li>Create a new book</li>
 *   <li>Retrieve a book by ID or ISBN</li>
 *   <li>Retrieve all books</li>
 *   <li>Update a book's stock quantity</li>
 *   <li>Update a book's price</li>
 *   <li>Delete a book</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book Management", description = "Operations for managing books in the bookstore")
public class BookControllerV1 {
    private final BookService bookService;

    /**
     * Constructs a new BookController with the specified book service.
     *
     * @param bookService the service to handle book-related business logic
     */
    public BookControllerV1(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Creates a new book in the system.
     *
     * @param request the data transfer object containing book details
     * @return a ResponseEntity containing the created BookDTO with HTTP status 201 (CREATED)
     */
    @PostMapping
    @Operation(summary = "Create a new book", description = "Creates a new book with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully created",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Book with the same ISBN already exists")
    })
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody CreateBookRequest request) {
        BookDTO book = bookService.createBook(
                request.getIsbn(),
                request.getTitle(),
                request.getAuthorFirstName(),
                request.getAuthorLastName(),
                request.getPublisherName(),
                request.getPrice()
        );
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id the UUID of the book to retrieve
     * @return a ResponseEntity containing the BookDTO with HTTP status 200 (OK) if found,
     * or HTTP status 404 (NOT_FOUND) if no book exists with the given ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Retrieves a book using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookDTO> getBookById(@PathVariable UUID id) {
        return bookService.findBookById(id)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves a book by its ISBN (International Standard Book Number).
     *
     * @param isbn the ISBN of the book to retrieve
     * @return a ResponseEntity containing the BookDTO with HTTP status 200 (OK) if found,
     * or HTTP status 404 (NOT_FOUND) if no book exists with the given ISBN
     */
    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get a book by ISBN", description = "Retrieves a book using its International Standard Book Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findBookByIsbn(isbn)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves all books in the system.
     *
     * @return a ResponseEntity containing a list of BookDTOs with HTTP status 200 (OK)
     */
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves a list of all books in the system")
    @ApiResponse(responseCode = "200", description = "List of books retrieved successfully",
            content = @Content(schema = @Schema(implementation = BookDTO.class)))
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.findAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Updates the stock quantity of a specific book.
     *
     * @param id      the UUID of the book to update
     * @param request the data transfer object containing the new stock quantity
     * @return a ResponseEntity with HTTP status 200 (OK) if the update was successful
     */
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update book stock", description = "Updates the stock quantity of a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid stock quantity"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> updateBookStock(@PathVariable UUID id,
                                                @Valid @RequestBody UpdateBookStockRequest request) {
        bookService.updateBookStock(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates the price of a specific book.
     *
     * @param id      the UUID of the book to update
     * @param request the data transfer object containing the new price
     * @return a ResponseEntity with HTTP status 200 (OK) if the update was successful
     */
    @PatchMapping("/{id}/price")
    @Operation(summary = "Update book price", description = "Updates the price of a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid price value"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> updateBookPrice(@PathVariable UUID id,
                                                @Valid @RequestBody UpdateBookPriceRequest request) {
        bookService.updateBookPrice(id, request.getPrice());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes a book from the system.
     *
     * @param id the UUID of the book to delete
     * @return a ResponseEntity with HTTP status 204 (NO_CONTENT) indicating successful deletion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes all books from the system.
     *
     * @return a ResponseEntity with HTTP status 204 (NO_CONTENT) indicating successful deletion
     */
    @DeleteMapping
    @Operation(summary = "Delete all books", description = "Deletes all books from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All books successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Operation not permitted")
    })
    public ResponseEntity<Void> deleteAllBooks() {
        bookService.deleteAllBooks();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
