package com.example.Stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object for Category entity.
 * Used for API requests and responses to avoid exposing internal entity structure.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    /**
     * Unique identifier for the category.
     * Null for create requests, populated for responses.
     */
    private UUID categoryId;

    /**
     * Name of the category.
     * Required field with length constraints.
     */
    @NotBlank(message = "Category name is required")
    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    private String name;

    /**
     * Constructor for creating DTO without ID (for create requests).
     * 
     * @param name the category name
     */
    public CategoryDTO(String name) {
        this.name = name;
    }
}
