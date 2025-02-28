package com.aronim.bookstore.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Schema(description = "Request object for creating a new book")
public class CreateBookRequest {
    @Schema(description = "Book's ISBN", example = "978-0-7475-3269-9")
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$")
    String isbn;

    @Schema(description = "Book's title", example = "The Great Gatsby")
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200)
    String title;

    @Schema(description = "Author's first name", example = "F. Scott")
    @NotBlank(message = "Author's first name is required")
    String authorFirstName;

    @Schema(description = "Author's last name", example = "Fitzgerald")
    @NotBlank(message = "Author's last name is required")
    String authorLastName;

    @Schema(description = "Publisher's name", example = "Scribner")
    @NotBlank(message = "Publisher name is required")
    String publisherName;

    @Schema(description = "Book's price", example = "29.99")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    BigDecimal price;
}
