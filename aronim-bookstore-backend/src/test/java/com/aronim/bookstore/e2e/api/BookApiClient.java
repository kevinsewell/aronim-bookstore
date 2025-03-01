package com.aronim.bookstore.e2e.api;

import com.aronim.bookstore.domain.model.BookId;
import com.aronim.bookstore.presentation.request.CreateBookRequest;
import com.aronim.bookstore.presentation.request.UpdateBookPriceRequest;
import com.aronim.bookstore.presentation.request.UpdateBookStockRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Component
public class BookApiClient {

    @Autowired
    private Environment environment;

    private String getBaseUrl() {
        return "http://localhost:" + environment.getProperty("local.server.port") + "/api";
    }

    public Response createBook(CreateBookRequest request, String authToken) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(request)
                .when()
                .post(getBaseUrl() + "/books");
    }

    public Response getBookById(String bookId, String authToken) {
        return given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get(getBaseUrl() + "/books/" + bookId);
    }

    public Response getAllBooks(String authToken) {
        return given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get(getBaseUrl() + "/books");
    }

    public Response updateBookPrice(BookId bookId, UpdateBookPriceRequest request, String authToken) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(request)
                .when()
                .patch(getBaseUrl() + "/books/" + bookId.getValue() + "/price");
    }

    public Response updateBookStock(BookId bookId, UpdateBookStockRequest request, String authToken) {
        return updateBookStock(bookId.getValue(), request, authToken);
    }

    public Response updateBookStock(UUID bookId, UpdateBookStockRequest request, String authToken) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(request)
                .when()
                .patch(getBaseUrl() + "/books/" + bookId + "/stock");
    }
}
