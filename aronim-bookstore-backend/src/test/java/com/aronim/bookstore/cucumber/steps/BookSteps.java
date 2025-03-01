package com.aronim.bookstore.cucumber.steps;

import com.aronim.bookstore.application.dto.BookDTO;
import com.aronim.bookstore.domain.model.Book;
import com.aronim.bookstore.domain.model.ISBN;
import com.aronim.bookstore.domain.repository.BookRepository;
import com.aronim.bookstore.e2e.api.BookApiClient;
import com.aronim.bookstore.presentation.request.CreateBookRequest;
import com.aronim.bookstore.presentation.request.UpdateBookPriceRequest;
import com.aronim.bookstore.presentation.request.UpdateBookStockRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BookSteps {

    private final BookApiClient bookApiClient;

    private final BookRepository bookRepository;

    private final ResponseHolder responseHolder;

    private String authToken;

    public BookSteps(BookApiClient bookApiClient, BookRepository bookRepository, ResponseHolder responseHolder) {
        this.bookApiClient = bookApiClient;
        this.bookRepository = bookRepository;
        this.responseHolder = responseHolder;
    }

    @Given("I am logged in as {string} with password {string}")
    public void iAmLoggedInAs(String email, String password) {
        // Implementation to login and get auth token
        // This would use your UserApiClient
        authToken = "sample-auth-token"; // Replace with actual token
    }

    @Given("a book with ISBN {string} exists in the catalog with {int} items in stock")
    public void bookExistsWithStock(String isbn, int stock) {
        CreateBookRequest request = new CreateBookRequest(
                isbn,
                "Title",
                "Author First Name",
                "Author Last Name",
                "Publisher Name",
                new BigDecimal(10)
        );

        final Response response = bookApiClient.createBook(request, authToken);
        final BookDTO book = response.as(BookDTO.class);

        bookApiClient.updateBookStock(book.getId(), new UpdateBookStockRequest(stock), authToken);
    }

    @Given("a book with ISBN {string} exists in the catalog with price {double}")
    public void bookExistsWithPrice(String isbn, double price) {
        CreateBookRequest request = new CreateBookRequest(
                isbn,
                "Title",
                "Author First Name",
                "Author Last Name",
                "Publisher Name",
                new BigDecimal(price)
        );

        bookApiClient.createBook(request, authToken);
    }

    @Given("the following books exist in the catalog:")
    public void theBooksExistInCatalog(DataTable dataTable) {
        List<Map<String, String>> books = dataTable.asMaps();

        for (Map<String, String> bookData : books) {
            CreateBookRequest request = new CreateBookRequest(
                    bookData.get("isbn"),
                    bookData.get("title"),
                    bookData.get("authorFirstName"),
                    bookData.get("authorLastName"),
                    bookData.get("publisher"),
                    new BigDecimal(bookData.get("price"))
            );

            bookApiClient.createBook(request, authToken);
        }
    }

    @When("I create a book with the following details:")
    public void createBookWithDetails(DataTable dataTable) {
        Map<String, String> bookData = dataTable.asMap();

        CreateBookRequest request = new CreateBookRequest(
                bookData.get("isbn"),
                bookData.get("title"),
                bookData.get("authorFirstName"),
                bookData.get("authorLastName"),
                bookData.get("publisher"),
                new BigDecimal(bookData.get("price"))
        );

        responseHolder.response = bookApiClient.createBook(request, authToken);
    }

    @When("I request all books")
    public void requestAllBooks() {
        responseHolder.response = bookApiClient.getAllBooks(authToken);
    }

    @When("I update the stock of the book with ISBN {string} to {int}")
    public void updateBookStock(String isbn, int newStock) {
        // First get the book ID by ISBN
        Book book = bookRepository.findByIsbn(new ISBN(isbn))
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));

        UpdateBookStockRequest request = new UpdateBookStockRequest(newStock);
        responseHolder.response = bookApiClient.updateBookStock(book.getId(), request, authToken);
    }

    @When("I update the price of the book with ISBN {string} to {double}")
    public void updateBookPrice(String isbn, double newPrice) {
        // First get the book ID by ISBN
        Book book = bookRepository.findByIsbn(new ISBN(isbn))
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));

        UpdateBookPriceRequest request = new UpdateBookPriceRequest(BigDecimal.valueOf(newPrice));
        responseHolder.response = bookApiClient.updateBookPrice(book.getId(), request, authToken);
    }

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int expectedStatus) {
        assertThat(responseHolder.response.getStatusCode()).isEqualTo(expectedStatus);
    }

    @Then("the book with ISBN {string} should be available in the catalog")
    public void bookShouldBeAvailable(String isbn) {
        assertThat(bookRepository.findByIsbn(new ISBN(isbn)).isPresent()).isTrue();
    }

    @Then("the response should contain {int} books")
    public void responseShouldContainBooks(int count) {
        List<BookDTO> books = responseHolder.response.jsonPath().getList("$", BookDTO.class);
        assertThat(books).hasSize(count);
    }

    @Then("the response should include a book with ISBN {string}")
    public void responseShouldIncludeBook(String isbn) {
        List<BookDTO> books = responseHolder.response.jsonPath().getList("$", BookDTO.class);
        assertThat(books.stream().anyMatch(book -> book.getIsbn().equals(isbn))).isTrue();
    }

    @Then("the book with ISBN {string} should have {int} items in stock")
    public void bookShouldHaveStock(String isbn, int expectedStock) {
        Book book = bookRepository.findByIsbn(new ISBN(isbn))
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));

        assertThat(book.getStockQuantity()).isEqualTo(expectedStock);
    }

    @Then("the book with ISBN {string} should have price {double}")
    public void bookShouldHavePrice(String isbn, double expectedPrice) {
        Book book = bookRepository.findByIsbn(new ISBN(isbn))
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));

        assertThat(book.getPrice().doubleValue()).isEqualTo(expectedPrice);
    }

    @And("there are no existing books in the system")
    public void thereAreNoExistingBooksInTheSystem() {
        bookRepository.deleteAll();
    }
}
