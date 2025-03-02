package com.aronim.bookstore.catalog.application.eventhandler;

import com.aronim.bookstore.catalog.domain.event.BookCreatedEvent;
import com.aronim.bookstore.catalog.domain.event.BookStockUpdatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BookEventHandler {

    @EventListener
    public void handleBookCreated(BookCreatedEvent event) {
        // Handle book creation event
        // For example: Send notification, update cache, etc.
        System.out.println("Book created with ID: " + event.getBookId().getValue());
    }

    @EventListener
    public void handleBookStockUpdated(BookStockUpdatedEvent event) {
        // Handle stock update event
        // For example: Update inventory statistics, trigger reorder, etc.
        System.out.println("Book stock updated - BookID: " + event.getBookId().getValue() +
                ", Quantity Changed: " + event.getQuantity() +
                ", New Stock: " + event.getNewStock());
    }
}
