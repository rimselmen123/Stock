package com.example.Stock.controller;

import com.example.Stock.dto.CategoryDTO;
import com.example.Stock.entity.Category;
import com.example.Stock.mapper.CategoryMapper;
import com.example.Stock.services.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST Controller for Category management operations.
 * Provides CRUD endpoints for managing product categories in the inventory system.
 * 
 * Base URL: /api/categories
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Create a new category.
     * 
     * @param categoryDTO the category data to create
     * @return ResponseEntity with created category and HTTP 201 status
     */
    @PostMapping("/add")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            logger.info("Creating new category: {}", categoryDTO.getName());
            
            Category category = categoryMapper.toEntity(categoryDTO);
            Category savedCategory = categoryService.saveCategory(category);
            CategoryDTO responseDTO = categoryMapper.toDTO(savedCategory);
            
            logger.info("Successfully created category with ID: {}", savedCategory.getCategoryId());
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid category data: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error creating category: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all categories.
     * 
     * @return ResponseEntity with list of categories and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        try {
            logger.info("Retrieving all categories");
            
            List<Category> categories = categoryService.getAllCategories();
            List<CategoryDTO> categoryDTOs = categoryMapper.toDTOList(categories);
            
            logger.info("Successfully retrieved {} categories", categories.size());
            return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error retrieving categories: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a category by its ID.
     * 
     * @param categoryId the ID of the category to retrieve
     * @return ResponseEntity with category data and HTTP 200 status, or HTTP 404 if not found
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId) {
        try {
            logger.info("Retrieving category with ID: {}", categoryId);
            
            Optional<Category> categoryOpt = categoryService.getCategoryById(categoryId);
            if (categoryOpt.isPresent()) {
                CategoryDTO categoryDTO = categoryMapper.toDTO(categoryOpt.get());
                logger.info("Successfully retrieved category: {}", categoryDTO.getName());
                return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
            } else {
                logger.warn("Category not found with ID: {}", categoryId);
                return new ResponseEntity<>(new ErrorResponse("Category not found"), HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            logger.error("Error retrieving category with ID {}: {}", categoryId, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing category.
     * 
     * @param categoryId the ID of the category to update
     * @param categoryDTO the updated category data
     * @return ResponseEntity with updated category and HTTP 200 status
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable UUID categoryId, 
                                          @Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            logger.info("Updating category with ID: {}", categoryId);
            
            Category category = categoryMapper.toEntity(categoryDTO);
            Category updatedCategory = categoryService.updateCategory(categoryId, category);
            CategoryDTO responseDTO = categoryMapper.toDTO(updatedCategory);
            
            logger.info("Successfully updated category with ID: {}", categoryId);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid category data for update: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            logger.warn("Category not found for update with ID: {}", categoryId);
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating category with ID {}: {}", categoryId, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a category by its ID.
     * 
     * @param categoryId the ID of the category to delete
     * @return ResponseEntity with HTTP 204 status on success
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID categoryId) {
        try {
            logger.info("Deleting category with ID: {}", categoryId);
            
            categoryService.deleteCategory(categoryId);
            
            logger.info("Successfully deleted category with ID: {}", categoryId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (RuntimeException e) {
            logger.warn("Error deleting category with ID {}: {}", categoryId, e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error deleting category with ID {}: {}", categoryId, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Search categories by name.
     * 
     * @param query the search query (case-insensitive)
     * @return ResponseEntity with list of matching categories and HTTP 200 status
     */
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchCategories(@RequestParam String query) {
        try {
            logger.info("Searching categories with query: {}", query);
            
            List<Category> categories = categoryService.searchCategories(query);
            List<CategoryDTO> categoryDTOs = categoryMapper.toDTOList(categories);
            
            logger.info("Found {} categories matching query: {}", categories.size(), query);
            return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error searching categories with query {}: {}", query, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a category by its name.
     * 
     * @param name the name of the category to retrieve
     * @return ResponseEntity with category data and HTTP 200 status, or HTTP 404 if not found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
        try {
            logger.info("Retrieving category with name: {}", name);
            
            Optional<Category> categoryOpt = categoryService.getCategoryByName(name);
            if (categoryOpt.isPresent()) {
                CategoryDTO categoryDTO = categoryMapper.toDTO(categoryOpt.get());
                logger.info("Successfully retrieved category by name: {}", name);
                return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
            } else {
                logger.warn("Category not found with name: {}", name);
                return new ResponseEntity<>(new ErrorResponse("Category not found"), HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            logger.error("Error retrieving category with name {}: {}", name, e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check if a category exists by name.
     * 
     * @param name the name to check
     * @return ResponseEntity with boolean result and HTTP 200 status
     */
    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        try {
            logger.info("Checking if category exists with name: {}", name);
            
            boolean exists = categoryService.existsByName(name);
            
            logger.info("Category exists check for name {}: {}", name, exists);
            return new ResponseEntity<>(exists, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error checking category existence with name {}: {}", name, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get categories that have products.
     * 
     * @return ResponseEntity with list of categories with products and HTTP 200 status
     */
    @GetMapping("/with-products")
    public ResponseEntity<List<CategoryDTO>> getCategoriesWithProducts() {
        try {
            logger.info("Retrieving categories that have products");
            
            List<Category> categories = categoryService.getCategoriesWithProducts();
            List<CategoryDTO> categoryDTOs = categoryMapper.toDTOList(categories);
            
            logger.info("Successfully retrieved {} categories with products", categories.size());
            return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error retrieving categories with products: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
