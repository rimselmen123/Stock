package com.example.Stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for creating new products.
 * Simplified DTO focused on product creation with minimal required fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

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
     * Category ID for the product.
     * Optional field for associating product with a category.
     */
    private UUID categoryId;

    /**
     * List of tag IDs to associate with this product.
     * Optional field for flexible product categorization.
     */
    private List<UUID> tagIds;
}
