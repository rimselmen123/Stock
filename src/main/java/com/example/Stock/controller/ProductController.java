package com.example.Stock.controller;

import com.example.Stock.dto.ProductCreateDTO;
import com.example.Stock.dto.ProductDTO;
import com.example.Stock.dto.ProductUpdateDTO;
import com.example.Stock.exception.DuplicateResourceException;
import com.example.Stock.exception.ResourceNotFoundException;
import com.example.Stock.exception.ValidationException;
import com.example.Stock.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST Controller for Product management operations.
 * Provides CRUD endpoints for managing products in the inventory system.
 * 
 * Base URL: /api/products
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * Create a new product.
     * 
     * @param createDTO the product data to create
     * @return ResponseEntity with created product and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateDTO createDTO) {
        try {
            log.info("Creating new product: {}", createDTO.getName());
            
            ProductDTO createdProduct = productService.createProduct(createDTO);
            
            log.info("Successfully created product with ID: {}", createdProduct.getProductId());
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
            
        } catch (DuplicateResourceException e) {
            log.warn("Duplicate product data: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
        } catch (ValidationException e) {
            log.warn("Invalid product data: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            log.warn("Resource not found: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all products with pagination.
     * 
     * @param pageable pagination information
     * @return ResponseEntity with page of products and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(@PageableDefault(size = 20) Pageable pageable) {
        try {
            log.info("Retrieving all products with pagination");
            
            Page<ProductDTO> products = productService.getAllProducts(pageable);
            
            log.info("Successfully retrieved {} products", products.getTotalElements());
            return new ResponseEntity<>(products, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error retrieving products: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all products without pagination.
     * 
     * @return ResponseEntity with list of products and HTTP 200 status
     */
    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProductsNoPagination() {
        try {
            log.info("Retrieving all products without pagination");
            
            List<ProductDTO> products = productService.getAllProducts();
            
            log.info("Successfully retrieved {} products", products.size());
            return new ResponseEntity<>(products, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error retrieving products: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a product by its ID.
     * 
     * @param productId the ID of the product to retrieve
     * @return ResponseEntity with product data and HTTP 200 status, or HTTP 404 if not found
     */
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable UUID productId) {
        try {
            log.info("Retrieving product with ID: {}", productId);
            
            Optional<ProductDTO> productOpt = productService.getProductById(productId);
            if (productOpt.isPresent()) {
                ProductDTO product = productOpt.get();
                log.info("Successfully retrieved product: {}", product.getName());
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                log.warn("Product not found with ID: {}", productId);
                return new ResponseEntity<>(new ErrorResponse("Product not found"), HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            log.error("Error retrieving product with ID {}: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing product.
     * 
     * @param productId the ID of the product to update
     * @param updateDTO the updated product data
     * @return ResponseEntity with updated product and HTTP 200 status
     */
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID productId, 
                                         @Valid @RequestBody ProductUpdateDTO updateDTO) {
        try {
            log.info("Updating product with ID: {}", productId);
            
            ProductDTO updatedProduct = productService.updateProduct(productId, updateDTO);
            
            log.info("Successfully updated product with ID: {}", productId);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
            
        } catch (ResourceNotFoundException e) {
            log.warn("Product not found for update with ID: {}", productId);
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (DuplicateResourceException e) {
            log.warn("Duplicate product data: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
        } catch (ValidationException e) {
            log.warn("Invalid product data for update: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error updating product with ID {}: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a product by its ID.
     * 
     * @param productId the ID of the product to delete
     * @return ResponseEntity with HTTP 204 status on success
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID productId) {
        try {
            log.info("Deleting product with ID: {}", productId);
            
            productService.deleteProduct(productId);
            
            log.info("Successfully deleted product with ID: {}", productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (ResourceNotFoundException e) {
            log.warn("Product not found for deletion with ID: {}", productId);
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            log.warn("Cannot delete product with ID {}: {}", productId, e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error deleting product with ID {}: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Search products by name.
     * 
     * @param query the search query (case-insensitive)
     * @return ResponseEntity with list of matching products and HTTP 200 status
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<ProductDTO>> searchProductsByName(@RequestParam String query) {
        try {
            log.info("Searching products by name with query: {}", query);
            
            List<ProductDTO> products = productService.searchProductsByName(query);
            
            log.info("Found {} products matching name query: {}", products.size(), query);
            return new ResponseEntity<>(products, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error searching products by name with query {}: {}", query, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Search products by name or description.
     * 
     * @param searchText the search text (case-insensitive)
     * @return ResponseEntity with list of matching products and HTTP 200 status
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String searchText) {
        try {
            log.info("Searching products with text: {}", searchText);
            
            List<ProductDTO> products = productService.searchProducts(searchText);
            
            log.info("Found {} products matching search text: {}", products.size(), searchText);
            return new ResponseEntity<>(products, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error searching products with text {}: {}", searchText, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a product by its name.
     * 
     * @param name the name of the product to retrieve
     * @return ResponseEntity with product data and HTTP 200 status, or HTTP 404 if not found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name) {
        try {
            log.info("Retrieving product with name: {}", name);
            
            Optional<ProductDTO> productOpt = productService.getProductByName(name);
            if (productOpt.isPresent()) {
                ProductDTO product = productOpt.get();
                log.info("Successfully retrieved product by name: {}", name);
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                log.warn("Product not found with name: {}", name);
                return new ResponseEntity<>(new ErrorResponse("Product not found"), HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            log.error("Error retrieving product with name {}: {}", name, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a product by its barcode.
     * 
     * @param barcode the barcode of the product to retrieve
     * @return ResponseEntity with product data and HTTP 200 status, or HTTP 404 if not found
     */
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<?> getProductByBarcode(@PathVariable String barcode) {
        try {
            log.info("Retrieving product with barcode: {}", barcode);
            
            Optional<ProductDTO> productOpt = productService.getProductByBarcode(barcode);
            if (productOpt.isPresent()) {
                ProductDTO product = productOpt.get();
                log.info("Successfully retrieved product by barcode: {}", barcode);
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                log.warn("Product not found with barcode: {}", barcode);
                return new ResponseEntity<>(new ErrorResponse("Product not found"), HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            log.error("Error retrieving product with barcode {}: {}", barcode, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get products by category ID.
     *
     * @param categoryId the category ID
     * @return ResponseEntity with list of products and HTTP 200 status
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable UUID categoryId) {
        try {
            log.info("Retrieving products by category ID: {}", categoryId);

            List<ProductDTO> products = productService.getProductsByCategory(categoryId);

            log.info("Found {} products in category: {}", products.size(), categoryId);
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error retrieving products by category {}: {}", categoryId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get products by category name.
     *
     * @param categoryName the category name
     * @return ResponseEntity with list of products and HTTP 200 status
     */
    @GetMapping("/category/name/{categoryName}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryName(@PathVariable String categoryName) {
        try {
            log.info("Retrieving products by category name: {}", categoryName);

            List<ProductDTO> products = productService.getProductsByCategoryName(categoryName);

            log.info("Found {} products in category: {}", products.size(), categoryName);
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error retrieving products by category name {}: {}", categoryName, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get products by unit of measurement.
     *
     * @param unit the unit of measurement
     * @return ResponseEntity with list of products and HTTP 200 status
     */
    @GetMapping("/unit/{unit}")
    public ResponseEntity<List<ProductDTO>> getProductsByUnit(@PathVariable String unit) {
        try {
            log.info("Retrieving products by unit: {}", unit);

            List<ProductDTO> products = productService.getProductsByUnit(unit);

            log.info("Found {} products with unit: {}", products.size(), unit);
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error retrieving products by unit {}: {}", unit, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get products that have stock.
     *
     * @return ResponseEntity with list of products and HTTP 200 status
     */
    @GetMapping("/with-stock")
    public ResponseEntity<List<ProductDTO>> getProductsWithStock() {
        try {
            log.info("Retrieving products with stock");

            List<ProductDTO> products = productService.getProductsWithStock();

            log.info("Found {} products with stock", products.size());
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error retrieving products with stock: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get products that have no stock.
     *
     * @return ResponseEntity with list of products and HTTP 200 status
     */
    @GetMapping("/without-stock")
    public ResponseEntity<List<ProductDTO>> getProductsWithoutStock() {
        try {
            log.info("Retrieving products without stock");

            List<ProductDTO> products = productService.getProductsWithoutStock();

            log.info("Found {} products without stock", products.size());
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error retrieving products without stock: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check if a product exists by name.
     *
     * @param name the name to check
     * @return ResponseEntity with boolean result and HTTP 200 status
     */
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        try {
            log.info("Checking if product exists with name: {}", name);

            boolean exists = productService.existsByName(name);

            log.info("Product exists check for name {}: {}", name, exists);
            return new ResponseEntity<>(exists, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error checking product existence with name {}: {}", name, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check if a product exists by barcode.
     *
     * @param barcode the barcode to check
     * @return ResponseEntity with boolean result and HTTP 200 status
     */
    @GetMapping("/exists/barcode/{barcode}")
    public ResponseEntity<Boolean> existsByBarcode(@PathVariable String barcode) {
        try {
            log.info("Checking if product exists with barcode: {}", barcode);

            boolean exists = productService.existsByBarcode(barcode);

            log.info("Product exists check for barcode {}: {}", barcode, exists);
            return new ResponseEntity<>(exists, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error checking product existence with barcode {}: {}", barcode, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get products by tag.
     *
     * @param tagId the tag ID
     * @return ResponseEntity with list of products and HTTP 200 status
     */
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<?> getProductsByTag(@PathVariable UUID tagId) {
        try {
            log.info("Retrieving products by tag ID: {}", tagId);

            List<ProductDTO> products = productService.getProductsByTag(tagId);

            log.info("Found {} products with tag: {}", products.size(), tagId);
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            log.warn("Tag not found with ID: {}", tagId);
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error retrieving products by tag {}: {}", tagId, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add tags to a product.
     *
     * @param productId the product ID
     * @param tagIds the list of tag IDs to add
     * @return ResponseEntity with updated product and HTTP 200 status
     */
    @PostMapping("/{productId}/tags")
    public ResponseEntity<?> addTagsToProduct(@PathVariable UUID productId,
                                            @RequestBody List<UUID> tagIds) {
        try {
            log.info("Adding tags to product with ID: {}", productId);

            ProductDTO updatedProduct = productService.addTagsToProduct(productId, tagIds);

            log.info("Successfully added tags to product with ID: {}", productId);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            log.warn("Resource not found: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error adding tags to product {}: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Remove tags from a product.
     *
     * @param productId the product ID
     * @param tagIds the list of tag IDs to remove
     * @return ResponseEntity with updated product and HTTP 200 status
     */
    @DeleteMapping("/{productId}/tags")
    public ResponseEntity<?> removeTagsFromProduct(@PathVariable UUID productId,
                                                 @RequestBody List<UUID> tagIds) {
        try {
            log.info("Removing tags from product with ID: {}", productId);

            ProductDTO updatedProduct = productService.removeTagsFromProduct(productId, tagIds);

            log.info("Successfully removed tags from product with ID: {}", productId);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            log.warn("Resource not found: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error removing tags from product {}: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Error response class for consistent error handling.
     */
    public static class ErrorResponse {
        private String message;
        private long timestamp;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
