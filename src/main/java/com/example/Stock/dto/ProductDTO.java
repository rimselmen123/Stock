package com.example.Stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for Product entity.
 * Used for API requests and responses to avoid exposing internal entity structure.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    /**
     * Unique identifier for the product.
     * Null for create requests, populated for responses.
     */
    private UUID productId;

    /**
     * Name of the product.
     * Required field with length constraints.
     */
    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 100, message = "Product name must be between 1 and 100 characters")
    private String name;

    /**
     * Unique barcode for product identification.
     * Optional field with length constraints.
     */
    @Size(max = 50, message = "Barcode must not exceed 50 characters")
    private String barcode;

    /**
     * Unit of measurement for the product.
     * Optional field with length constraints.
     */
    @Size(max = 20, message = "Unit must not exceed 20 characters")
    private String unit;

    /**
     * Detailed description of the product.
     * Optional field for storing additional product information.
     */
    private String description;

    /**
     * Category information for the product.
     * Contains basic category details.
     */
    private CategoryDTO category;

    /**
     * List of tags associated with this product.
     * Used for flexible product categorization.
     */
    private List<TagDTO> tags;

    /**
     * Constructor for creating DTO without ID and relationships (for simple create requests).
     * 
     * @param name the product name
     * @param barcode the product barcode
     * @param unit the unit of measurement
     * @param description the product description
     */
    public ProductDTO(String name, String barcode, String unit, String description) {
        this.name = name;
        this.barcode = barcode;
        this.unit = unit;
        this.description = description;
    }
}
