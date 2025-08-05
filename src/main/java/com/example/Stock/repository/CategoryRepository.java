package com.example.Stock.repository;

import com.example.Stock.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Category entity operations.
 * Provides CRUD operations and custom queries for managing product categories
 * in the inventory management system.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /**
     * Find a category by its name.
     * 
     * @param name the name of the category
     * @return Optional containing the category if found
     */
    Optional<Category> findByName(String name);

    /**
     * Find categories whose names contain the specified text (case-insensitive).
     * 
     * @param name the text to search for in category names
     * @return list of categories matching the search criteria
     */
    List<Category> findByNameContainingIgnoreCase(String name);

    /**
     * Check if a category with the given name exists.
     * 
     * @param name the name to check
     * @return true if a category with this name exists
     */
    boolean existsByName(String name);

    /**
     * Find all categories that have products associated with them.
     * 
     * @return list of categories that contain products
     */
    @Query("SELECT DISTINCT c FROM Category c JOIN c.products p")
    List<Category> findCategoriesWithProducts();

    /**
     * Find categories ordered by name alphabetically.
     * 
     * @return list of categories sorted by name
     */
    List<Category> findAllByOrderByNameAsc();
}
