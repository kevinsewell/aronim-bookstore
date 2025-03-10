package com.aronim.bookstore.platform.api.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Schema(description = "Error response details")
public class ErrorResponse {
    @Schema(description = "Error timestamp", example = "2023-12-20T10:15:30")
    LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "400")
    int status;

    @Schema(description = "Error message", example = "Invalid input provided")
    String message;

    @Schema(description = "Error details", example = "ISBN must be a valid ISBN-13 format")
    String details;

    public ErrorResponse(int status, String message, String details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.details = details;
    }
}
