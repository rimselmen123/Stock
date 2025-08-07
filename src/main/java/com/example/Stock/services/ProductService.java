package com.example.Stock.services;

import com.example.Stock.dto.ProductCreateDTO;
import com.example.Stock.dto.ProductDTO;
import com.example.Stock.dto.ProductUpdateDTO;
import com.example.Stock.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for Product entity operations.
 * Provides business logic for managing products in the inventory system.
 */
public interface ProductService {

    /**
     * Create a new product.
     *
     * @param createDTO the product data to create
     * @return the created product DTO
     * @throws com.example.Stock.exception.DuplicateResourceException if product name or barcode already exists
     * @throws com.example.Stock.exception.ValidationException if product data is invalid
     */
    ProductDTO createProduct(ProductCreateDTO createDTO);

    /**
     * Update an existing product.
     *
     * @param productId the ID of the product to update
     * @param updateDTO the updated product data
     * @return the updated product DTO
     * @throws com.example.Stock.exception.ResourceNotFoundException if product not found
     * @throws com.example.Stock.exception.DuplicateResourceException if updated name or barcode conflicts
     * @throws com.example.Stock.exception.ValidationException if product data is invalid
     */
    ProductDTO updateProduct(UUID productId, ProductUpdateDTO updateDTO);

    /**
     * Get a product by its ID.
     *
     * @param productId the ID of the product
     * @return Optional containing the product DTO if found
     */
    Optional<ProductDTO> getProductById(UUID productId);

    /**
     * Get a product by its name.
     *
     * @param name the name of the product
     * @return Optional containing the product DTO if found
     */
    Optional<ProductDTO> getProductByName(String name);

    /**
     * Get a product by its barcode.
     *
     * @param barcode the barcode of the product
     * @return Optional containing the product DTO if found
     */
    Optional<ProductDTO> getProductByBarcode(String barcode);

    /**
     * Get all products with pagination.
     *
     * @param pageable pagination information
     * @return page of product DTOs
     */
    Page<ProductDTO> getAllProducts(Pageable pageable);

    /**
     * Get all products without pagination.
     *
     * @return list of all product DTOs
     */
    List<ProductDTO> getAllProducts();

    /**
     * Delete a product by its ID.
     *
     * @param productId the ID of the product to delete
     * @throws com.example.Stock.exception.ResourceNotFoundException if product not found
     * @throws com.example.Stock.exception.ValidationException if product cannot be deleted due to dependencies
     */
    void deleteProduct(UUID productId);

    /**
     * Search products by name (case-insensitive).
     *
     * @param query the search query
     * @return list of product DTOs matching the query
     */
    List<ProductDTO> searchProductsByName(String query);

    /**
     * Search products by name or description (case-insensitive).
     *
     * @param searchText the search text
     * @return list of product DTOs matching the search criteria
     */
    List<ProductDTO> searchProducts(String searchText);

    /**
     * Get products by category ID.
     *
     * @param categoryId the category ID
     * @return list of product DTOs in the specified category
     */
    List<ProductDTO> getProductsByCategory(UUID categoryId);

    /**
     * Get products by category name.
     *
     * @param categoryName the category name
     * @return list of product DTOs in the specified category
     */
    List<ProductDTO> getProductsByCategoryName(String categoryName);

    /**
     * Get products by unit of measurement.
     *
     * @param unit the unit of measurement
     * @return list of product DTOs with the specified unit
     */
    List<ProductDTO> getProductsByUnit(String unit);

    /**
     * Get products that have stock in any location.
     *
     * @return list of product DTOs with stock
     */
    List<ProductDTO> getProductsWithStock();

    /**
     * Get products that have no stock in any location.
     *
     * @return list of product DTOs without stock
     */
    List<ProductDTO> getProductsWithoutStock();

    /**
     * Check if a product exists by name.
     *
     * @param name the name to check
     * @return true if product exists
     */
    boolean existsByName(String name);

    /**
     * Check if a product exists by barcode.
     *
     * @param barcode the barcode to check
     * @return true if product exists
     */
    boolean existsByBarcode(String barcode);

    /**
     * Get products by tag.
     *
     * @param tagId the tag ID
     * @return list of product DTOs with the specified tag
     */
    List<ProductDTO> getProductsByTag(UUID tagId);

    /**
     * Add tags to a product.
     *
     * @param productId the product ID
     * @param tagIds the list of tag IDs to add
     * @return the updated product DTO
     * @throws com.example.Stock.exception.ResourceNotFoundException if product or tags not found
     */
    ProductDTO addTagsToProduct(UUID productId, List<UUID> tagIds);

    /**
     * Remove tags from a product.
     *
     * @param productId the product ID
     * @param tagIds the list of tag IDs to remove
     * @return the updated product DTO
     * @throws com.example.Stock.exception.ResourceNotFoundException if product not found
     */
    ProductDTO removeTagsFromProduct(UUID productId, List<UUID> tagIds);

    /**
     * Get the underlying Product entity by ID (for internal use).
     *
     * @param productId the product ID
     * @return the Product entity
     * @throws com.example.Stock.exception.ResourceNotFoundException if product not found
     */
    Product getProductEntityById(UUID productId);
}