package com.aronim.bookstore.presentation.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class UpdateBookPriceRequest {
    @NotNull(message = "Price is required")
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
