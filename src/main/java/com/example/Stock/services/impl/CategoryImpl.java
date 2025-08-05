package com.example.Stock.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Stock.entity.Category;
import com.example.Stock.repository.CategoryRepository;
import com.example.Stock.services.CategoryService;
/**
 * Implementation of CategoryService providing business logic for category management.
 * Includes validation, exception handling, and logging for production use.
 */
@Service
public class CategoryImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryImpl.class);
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category saveCategory(Category category) {
        logger.debug("Saving new category: {}", category.getName());

        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        // Check if category with same name already exists
        if (categoryRepository.existsByName(category.getName().trim())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }

        category.setName(category.getName().trim());
        Category savedCategory = categoryRepository.save(category);
        logger.info("Successfully saved category with ID: {}", savedCategory.getCategoryId());

        return savedCategory;
    }

    @Override
    public Category updateCategory(UUID categoryId, Category category) {
        logger.debug("Updating category with ID: {}", categoryId);

        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }

        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        Category existingCategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        // Check if another category with same name exists (excluding current one)
        Optional<Category> categoryWithSameName = categoryRepository.findByName(category.getName().trim());
        if (categoryWithSameName.isPresent() && !categoryWithSameName.get().getCategoryId().equals(categoryId)) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }

        existingCategory.setName(category.getName().trim());
        Category updatedCategory = categoryRepository.save(existingCategory);
        logger.info("Successfully updated category with ID: {}", categoryId);

        return updatedCategory;
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        logger.debug("Finding category by name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }

        return categoryRepository.findByName(name.trim());
    }

    @Override
    public void deleteCategory(UUID categoryId) {
        logger.debug("Deleting category with ID: {}", categoryId);

        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        // Check if category has products before deletion
        List<Category> categoriesWithProducts = categoryRepository.findCategoriesWithProducts();
        if (categoriesWithProducts.contains(category)) {
            throw new RuntimeException("Cannot delete category '" + category.getName() + "' because it has associated products");
        }

        categoryRepository.deleteById(categoryId);
        logger.info("Successfully deleted category with ID: {}", categoryId);
    }

    @Override
    public Optional<Category> getCategoryById(UUID categoryId) {
        logger.debug("Finding category by ID: {}", categoryId);

        if (categoryId == null) {
            return Optional.empty();
        }

        return categoryRepository.findById(categoryId);
    }

    @Override
    public List<Category> getAllCategories() {
        logger.debug("Retrieving all categories");
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        logger.info("Found {} categories", categories.size());
        return categories;
    }

    @Override
    public List<Category> searchCategories(String query) {
        logger.debug("Searching categories with query: {}", query);

        if (query == null || query.trim().isEmpty()) {
            return getAllCategories();
        }

        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(query.trim());
        logger.info("Found {} categories matching query: {}", categories.size(), query);
        return categories;
    }

    @Override
    public boolean existsByName(String name) {
        logger.debug("Checking if category exists with name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        return categoryRepository.existsByName(name.trim());
    }

    @Override
    public List<Category> getCategoriesWithProducts() {
        logger.debug("Retrieving categories that have products");
        List<Category> categories = categoryRepository.findCategoriesWithProducts();
        logger.info("Found {} categories with products", categories.size());
        return categories;
    }
}