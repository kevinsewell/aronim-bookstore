package com.aronim.bookstore.catalog.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for updating a book's price")
public class UpdateBookPriceRequest {
    @Schema(description = "The new price for the book", requiredMode = Schema.RequiredMode.REQUIRED, example = "19.99")
    @NotNull(message = "Price is required")
    private BigDecimal price;
}
