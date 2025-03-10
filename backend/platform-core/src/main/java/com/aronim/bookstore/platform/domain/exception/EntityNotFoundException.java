package com.aronim.bookstore.platform.domain.exception;

/**
 * Exception thrown when a requested entity cannot be found.
 * <p>
 * This runtime exception is typically thrown when attempting to access or
 * manipulate an entity that does not exist in the system, such as when
 * looking up an entity by its identifier.
 * </p>
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs a new EntityNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
