package com.example.Stock.exception;

/**
 * Exception thrown when business validation rules are violated.
 * This exception is used for custom validation logic beyond basic field validation.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new ValidationException with the specified detail message.
     * 
     * @param message the detail message explaining the validation failure
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and cause.
     * 
     * @param message the detail message explaining the validation failure
     * @param cause the cause of this exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
