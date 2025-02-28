package com.aronim.bookstore.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateBookPriceRequest {
    @NotNull(message = "Price is required")
    private BigDecimal price;

}
