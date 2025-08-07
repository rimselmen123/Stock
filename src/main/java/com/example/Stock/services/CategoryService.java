package com.example.Stock.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.Stock.entity.Category;

/**
 * Service interface for Category entity operations.
 * Provides business logic for managing product categories in the inventory system.
 */
public interface CategoryService {

    /**
     * Save a new category to the database.
     *
     * @param category the category to save
     * @return the saved category with generated ID
     * @throws IllegalArgumentException if category is null or invalid
     */
    Category saveCategory(Category category);

    /**
     * Update an existing category.
     *
     * @param categoryId the ID of the category to update
     * @param category the updated category data
     * @return the updated category
     * @throws IllegalArgumentException if category is null or invalid
     * @throws RuntimeException if category not found
     */
    Category updateCategory(UUID categoryId, Category category);

    /**
     * Find a category by its name.
     *
     * @param name the name to search for
     * @return Optional containing the category if found
     */
    Optional<Category> getCategoryByName(String name);

    /**
     * Delete a category by its ID.
     *
     * @param categoryId the ID of the category to delete
     * @throws RuntimeException if category not found
     */
    void deleteCategory(UUID categoryId);

    /**
     * Get a category by its ID.
     *
     * @param categoryId the ID of the category
     * @return Optional containing the category if found
     */
    Optional<Category> getCategoryById(UUID categoryId);

    /**
     * Get all categories.
     *
     * @return list of all categories
     */
    List<Category> getAllCategories();

    /**
     * Search categories by name (case-insensitive).
     *
     * @param query the search query
     * @return list of categories matching the query
     */
    List<Category> searchCategories(String query);

    /**
     * Check if a category exists by name.
     *
     * @param name the name to check
     * @return true if category exists
     */
    boolean existsByName(String name);

    /**
     * Get categories that have products.
     *
     * @return list of categories with products
     */
    List<Category> getCategoriesWithProducts();
    

}

