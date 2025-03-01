package com.aronim.bookstore.presentation.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
@Schema(description = "Request object for updating a book's stock quantity")
public class UpdateBookStockRequest {

    @Schema(
            description = "The quantity to adjust the stock by. Use positive numbers to add stock and negative numbers to remove stock.",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "-999999",
            maximum = "999999"
    )
    @NotNull(message = "Quantity is required")
    Integer quantity;

    @JsonCreator
    public UpdateBookStockRequest(Integer quantity) {
        this.quantity = quantity;
    }
}
