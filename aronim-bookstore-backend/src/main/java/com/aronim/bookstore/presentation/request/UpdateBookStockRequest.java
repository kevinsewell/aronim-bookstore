package com.aronim.bookstore.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookStockRequest {
    @NotNull(message = "Quantity is required")
    private Integer quantity;

}
