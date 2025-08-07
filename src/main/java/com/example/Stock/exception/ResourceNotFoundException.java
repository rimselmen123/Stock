package com.example.Stock.exception;

/**
 * Exception thrown when a requested resource is not found in the system.
 * This is a runtime exception that should be caught and handled appropriately
 * by the controller layer to return proper HTTP status codes.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * 
     * @param message the detail message explaining what resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     * 
     * @param message the detail message explaining what resource was not found
     * @param cause the cause of this exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ResourceNotFoundException for a specific resource type and ID.
     * 
     * @param resourceType the type of resource that was not found
     * @param resourceId the ID of the resource that was not found
     */
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(String.format("%s not found with ID: %s", resourceType, resourceId));
    }
}
