package com.aronim.bookstore.presentation.request;

import jakarta.validation.constraints.NotNull;

public class UpdateBookStockRequest {
    @NotNull(message = "Quantity is required")
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
