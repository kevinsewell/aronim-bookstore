package com.aronim.bookstore.presentation.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Schema(description = "Request object for updating a book's price")
public class UpdateBookPriceRequest {
    @Schema(description = "The new price for the book", requiredMode = Schema.RequiredMode.REQUIRED, example = "19.99")
    @NotNull(message = "Price is required")
    BigDecimal price;

    @JsonCreator
    public UpdateBookPriceRequest(BigDecimal price) {
        this.price = price;
    }
}
