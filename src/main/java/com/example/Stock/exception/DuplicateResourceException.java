package com.example.Stock.exception;

/**
 * Exception thrown when attempting to create a resource that already exists
 * or when a unique constraint would be violated.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     * 
     * @param message the detail message explaining what resource is duplicated
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateResourceException with the specified detail message and cause.
     * 
     * @param message the detail message explaining what resource is duplicated
     * @param cause the cause of this exception
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DuplicateResourceException for a specific resource type and field.
     * 
     * @param resourceType the type of resource that is duplicated
     * @param fieldName the field that has the duplicate value
     * @param fieldValue the duplicate value
     */
    public DuplicateResourceException(String resourceType, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' already exists", resourceType, fieldName, fieldValue));
    }
}
