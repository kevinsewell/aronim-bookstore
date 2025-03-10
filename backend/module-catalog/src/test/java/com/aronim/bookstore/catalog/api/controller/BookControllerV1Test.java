package com.aronim.bookstore.catalog.api.controller;

import com.aronim.bookstore.catalog.api.request.CreateBookRequest;
import com.aronim.bookstore.catalog.api.request.UpdateBookPriceRequest;
import com.aronim.bookstore.catalog.api.request.UpdateBookStockRequest;
import com.aronim.bookstore.catalog.application.dto.BookDto;
import com.aronim.bookstore.catalog.application.service.BookService;
import com.aronim.bookstore.security.test.WithMockJwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BookControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

    @Test
    @WithMockJwt(subject = "admin", roles = {"ADMIN", "USER"})
    public void testCreateBookEndpoint() throws Exception {
        // Arrange
        CreateBookRequest request = createTestBookRequest();

        // Act & Assert
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.isbn", is(request.getIsbn())))
                .andExpect(jsonPath("$.title", is(request.getTitle())))
                .andExpect(jsonPath("$.authorFirstName", is(request.getAuthorFirstName())))
                .andExpect(jsonPath("$.authorLastName", is(request.getAuthorLastName())))
                .andExpect(jsonPath("$.publisher", is(request.getPublisherName())))
                .andExpect(jsonPath("$.price", is(request.getPrice().doubleValue())))
                .andExpect(jsonPath("$.stockQuantity", is(0)))
                .andExpect(jsonPath("$.status", is("AVAILABLE")))
                .andReturn();
    }

    @Test
    @WithMockJwt()
    public void testGetBookByIdEndpoint() throws Exception {
        // Arrange
        BookDto createdBook = createTestBook();
        String bookId = createdBook.getId().toString();

        // Act & Assert
        mockMvc.perform(get("/api/v1/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookId)))
                .andExpect(jsonPath("$.isbn", is(createdBook.getIsbn())))
                .andExpect(jsonPath("$.title", is(createdBook.getTitle())))
                .andExpect(jsonPath("$.authorFirstName", is(createdBook.getAuthorFirstName())))
                .andExpect(jsonPath("$.authorLastName", is(createdBook.getAuthorLastName())))
                .andExpect(jsonPath("$.publisher", is(createdBook.getPublisher())));
    }

    @Test
    @WithMockJwt
    public void testGetBookByIsbnEndpoint() throws Exception {
        // Arrange
        BookDto createdBook = createTestBook();

        // Act & Assert
        mockMvc.perform(get("/api/v1/books/isbn/{isbn}", createdBook.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdBook.getId().toString())))
                .andExpect(jsonPath("$.isbn", is(createdBook.getIsbn())))
                .andExpect(jsonPath("$.title", is(createdBook.getTitle())));
    }

    @Test
    @WithMockJwt
    public void testGetAllBooksEndpoint() throws Exception {
        // Arrange
        BookDto book1 = createTestBook();
        BookDto book2 = createTestBook("9780134685991", "Effective Java", "Joshua", "Bloch", "Addison-Wesley", new BigDecimal("59.99"));

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andReturn();

        List<BookDto> books = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class)
        );

        assertThat(books).extracting(BookDto::getIsbn)
                .contains(book1.getIsbn(), book2.getIsbn());
    }

    @Test
    @WithMockJwt(subject = "admin", roles = {"ADMIN", "USER"})
    public void testUpdateBookStockEndpoint() throws Exception {
        // Arrange
        BookDto createdBook = createTestBook();
        UpdateBookStockRequest updateRequest = new UpdateBookStockRequest(10);

        // Act & Assert - Update stock
        mockMvc.perform(patch("/api/v1/books/{id}/stock", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Verify the update
        mockMvc.perform(get("/api/v1/books/{id}", createdBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity", is(10)))
                .andExpect(jsonPath("$.status", is("AVAILABLE")));
    }

    @Test
    @WithMockJwt(subject = "admin", roles = {"ADMIN", "USER"})
    public void testUpdateBookPriceEndpoint() throws Exception {
        // Arrange
        BookDto createdBook = createTestBook();
        UpdateBookPriceRequest updateRequest = new UpdateBookPriceRequest(
                new BigDecimal("29.99")
        );

        // Act & Assert - Update price
        mockMvc.perform(patch("/api/v1/books/{id}/price", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Verify the update
        mockMvc.perform(get("/api/v1/books/{id}", createdBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(29.99)));
    }

    @Test
    @WithMockJwt(subject = "admin", roles = {"ADMIN", "USER"})
    public void testDeleteBookEndpoint() throws Exception {
        // Arrange
        BookDto createdBook = createTestBook();

        // Act & Assert - Delete book
        mockMvc.perform(delete("/api/v1/books/{id}", createdBook.getId()))
                .andExpect(status().isNoContent());

        // Verify it's gone
        mockMvc.perform(get("/api/v1/books/{id}", createdBook.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockJwt(subject = "admin", roles = {"ADMIN", "USER"})
    public void testCreateBookWithInvalidDataShouldReturnBadRequest() throws Exception {
        // Arrange
        CreateBookRequest request = new CreateBookRequest(
                "", // Invalid - should not be blank
                "Test Title",
                "John",
                "Doe",
                "Test Publisher",
                new BigDecimal("19.99")
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockJwt(subject = "admin", roles = {"ADMIN", "USER"})
    public void testStockUpdateToNegativeValueShouldFail() throws Exception {
        // Arrange
        BookDto createdBook = createTestBook();
        UpdateBookStockRequest updateRequest = new UpdateBookStockRequest(
                -10 // Attempting to remove more than available
        );

        // Act & Assert
        mockMvc.perform(patch("/api/v1/books/{id}/stock", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", containsString("Cannot remove more books than available in stock")));
    }

    @Test
    @WithMockJwt(subject = "admin", roles = {"ADMIN", "USER"})
    public void testBookStatusChangesToOutOfStockWhenQuantityIsZero() throws Exception {
        // Arrange
        BookDto createdBook = createTestBook();

        // First add some stock
        UpdateBookStockRequest addStockRequest = new UpdateBookStockRequest(10);
        mockMvc.perform(patch("/api/v1/books/{id}/stock", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addStockRequest)))
                .andExpect(status().isOk());

        // Then remove all stock
        UpdateBookStockRequest removeStockRequest = new UpdateBookStockRequest(0);

        // Act & Assert - Update stock to zero
        mockMvc.perform(patch("/api/v1/books/{id}/stock", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeStockRequest)))
                .andExpect(status().isOk());

        // Verify the status changed
        mockMvc.perform(get("/api/v1/books/{id}", createdBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity", is(0)))
                .andExpect(jsonPath("$.status", is("OUT_OF_STOCK")));
    }

    // Helper methods

    private CreateBookRequest createTestBookRequest() {
        return createTestBookRequest(
                "9781617294945",
                "Spring in Action",
                "Craig",
                "Walls",
                "Manning Publications",
                new BigDecimal("49.99")
        );
    }

    private CreateBookRequest createTestBookRequest(
            String isbn, String title, String authorFirstName, String authorLastName,
            String publisher, BigDecimal price) {

        return new CreateBookRequest(
                isbn,
                title,
                authorFirstName,
                authorLastName,
                publisher,
                price
        );
    }

    private BookDto createTestBook() {
        return createTestBook(
                "9781617294945",
                "Spring in Action",
                "Craig",
                "Walls",
                "Manning Publications",
                new BigDecimal("49.99")
        );
    }

    private BookDto createTestBook(
            String isbn, String title, String authorFirstName, String authorLastName,
            String publisherName, BigDecimal price) {

        return bookService.createBook(
                isbn,
                title,
                authorFirstName,
                authorLastName,
                publisherName,
                price
        );
    }
}
