package com.aronim.bookstore.presentation.controller;

import com.aronim.bookstore.application.dto.BookDTO;
import com.aronim.bookstore.presentation.request.CreateBookRequest;
import com.aronim.bookstore.presentation.request.UpdateBookPriceRequest;
import com.aronim.bookstore.presentation.request.UpdateBookStockRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateBookEndpoint() throws Exception {
        // Arrange
        CreateBookRequest request = createTestBookRequest();

        // Act & Assert
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.isbn", is(request.getIsbn())))
                .andExpect(jsonPath("$.title", is(request.getTitle())))
                .andExpect(jsonPath("$.author", is(request.getAuthorFirstName() + " " + request.getAuthorLastName())))
                .andExpect(jsonPath("$.publisher", is(request.getPublisher())))
                .andExpect(jsonPath("$.price", is(request.getPrice().doubleValue())))
                .andExpect(jsonPath("$.stockQuantity", is(0)))
                .andExpect(jsonPath("$.status", is("AVAILABLE")))
                .andReturn();
    }

    @Test
    @WithMockUser
    public void testGetBookByIdEndpoint() throws Exception {
        // Arrange
        BookDTO createdBook = createTestBook();
        String bookId = createdBook.getId().toString();

        // Act & Assert
        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookId)))
                .andExpect(jsonPath("$.isbn", is(createdBook.getIsbn())))
                .andExpect(jsonPath("$.title", is(createdBook.getTitle())))
                .andExpect(jsonPath("$.author", is(createdBook.getAuthor())))
                .andExpect(jsonPath("$.publisher", is(createdBook.getPublisher())));
    }

    @Test
    @WithMockUser
    public void testGetBookByIsbnEndpoint() throws Exception {
        // Arrange
        BookDTO createdBook = createTestBook();

        // Act & Assert
        mockMvc.perform(get("/api/books/isbn/{isbn}", createdBook.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdBook.getId().toString())))
                .andExpect(jsonPath("$.isbn", is(createdBook.getIsbn())))
                .andExpect(jsonPath("$.title", is(createdBook.getTitle())));
    }

    @Test
    @WithMockUser
    public void testGetAllBooksEndpoint() throws Exception {
        // Arrange
        BookDTO book1 = createTestBook();
        BookDTO book2 = createTestBook("9780134685991", "Effective Java", "Joshua", "Bloch", "Addison-Wesley", new BigDecimal("59.99"));

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andReturn();

        List<BookDTO> books = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookDTO.class)
        );

        assertThat(books).extracting(BookDTO::getIsbn)
                .contains(book1.getIsbn(), book2.getIsbn());
    }

    @Test
    @WithMockUser
    public void testUpdateBookStockEndpoint() throws Exception {
        // Arrange
        BookDTO createdBook = createTestBook();
        UpdateBookStockRequest updateRequest = new UpdateBookStockRequest();
        updateRequest.setQuantity(10);

        // Act & Assert - Update stock
        mockMvc.perform(patch("/api/books/{id}/stock", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Verify the update
        mockMvc.perform(get("/api/books/{id}", createdBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity", is(10)))
                .andExpect(jsonPath("$.status", is("AVAILABLE")));
    }

    @Test
    @WithMockUser
    public void testUpdateBookPriceEndpoint() throws Exception {
        // Arrange
        BookDTO createdBook = createTestBook();
        UpdateBookPriceRequest updateRequest = new UpdateBookPriceRequest();
        updateRequest.setPrice(new BigDecimal("29.99"));

        // Act & Assert - Update price
        mockMvc.perform(patch("/api/books/{id}/price", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Verify the update
        mockMvc.perform(get("/api/books/{id}", createdBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(29.99)));
    }

    @Test
    @WithMockUser
    public void testDeleteBookEndpoint() throws Exception {
        // Arrange
        BookDTO createdBook = createTestBook();

        // Act & Assert - Delete book
        mockMvc.perform(delete("/api/books/{id}", createdBook.getId()))
                .andExpect(status().isNoContent());

        // Verify it's gone
        mockMvc.perform(get("/api/books/{id}", createdBook.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testCreateBookWithInvalidDataShouldReturnBadRequest() throws Exception {
        // Arrange
        CreateBookRequest request = new CreateBookRequest();
        request.setIsbn(""); // Invalid - should not be blank
        request.setTitle("Test Title");
        request.setAuthorFirstName("John");
        request.setAuthorLastName("Doe");
        request.setPublisher("Test Publisher");
        request.setPrice(new BigDecimal("19.99"));

        // Act & Assert
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testStockUpdateToNegativeValueShouldFail() throws Exception {
        // Arrange
        BookDTO createdBook = createTestBook();
        UpdateBookStockRequest updateRequest = new UpdateBookStockRequest();
        updateRequest.setQuantity(-10); // Attempting to remove more than available

        // Act & Assert
        mockMvc.perform(patch("/api/books/{id}/stock", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Cannot remove more books than available in stock")));
    }

    @Test
    @WithMockUser
    public void testBookStatusChangesToOutOfStockWhenQuantityIsZero() throws Exception {
        // Arrange
        BookDTO createdBook = createTestBook();

        // First add some stock
        UpdateBookStockRequest addStockRequest = new UpdateBookStockRequest();
        addStockRequest.setQuantity(10);
        mockMvc.perform(patch("/api/books/{id}/stock", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addStockRequest)))
                .andExpect(status().isOk());

        // Then remove all stock
        UpdateBookStockRequest removeStockRequest = new UpdateBookStockRequest();
        removeStockRequest.setQuantity(-10);

        // Act & Assert - Update stock to zero
        mockMvc.perform(patch("/api/books/{id}/stock", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeStockRequest)))
                .andExpect(status().isOk());

        // Verify the status changed
        mockMvc.perform(get("/api/books/{id}", createdBook.getId()))
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

        CreateBookRequest request = new CreateBookRequest();
        request.setIsbn(isbn);
        request.setTitle(title);
        request.setAuthorFirstName(authorFirstName);
        request.setAuthorLastName(authorLastName);
        request.setPublisher(publisher);
        request.setPrice(price);
        return request;
    }

    private BookDTO createTestBook() throws Exception {
        return createTestBook(
                "9781617294945",
                "Spring in Action",
                "Craig",
                "Walls",
                "Manning Publications",
                new BigDecimal("49.99")
        );
    }

    private BookDTO createTestBook(
            String isbn, String title, String authorFirstName, String authorLastName,
            String publisher, BigDecimal price) throws Exception {

        CreateBookRequest request = createTestBookRequest(isbn, title, authorFirstName, authorLastName, publisher, price);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), BookDTO.class);
    }
}
