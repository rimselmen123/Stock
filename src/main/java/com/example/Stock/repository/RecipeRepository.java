package com.example.Stock.repository;

import com.example.Stock.entity.Product;
import com.example.Stock.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Recipe entity operations.
 * Provides CRUD operations and custom queries for managing recipes
 * in the inventory management system.
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    /**
     * Find recipe by product.
     * 
     * @param product the product to search for
     * @return Optional containing the recipe if found
     */
    Optional<Recipe> findByProduct(Product product);

    /**
     * Find recipe by product ID.
     * 
     * @param productId the product ID
     * @return Optional containing the recipe if found
     */
    Optional<Recipe> findByProductProductId(UUID productId);

    /**
     * Find recipe by name.
     * 
     * @param name the name of the recipe
     * @return Optional containing the recipe if found
     */
    Optional<Recipe> findByName(String name);

    /**
     * Find recipes whose names contain the specified text (case-insensitive).
     * 
     * @param name the text to search for in recipe names
     * @return list of recipes matching the search criteria
     */
    List<Recipe> findByNameContainingIgnoreCase(String name);

    /**
     * Find recipes created after a specific date.
     * 
     * @param date the date to compare against
     * @return list of recipes created after the specified date
     */
    List<Recipe> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find recipes updated after a specific date.
     * 
     * @param date the date to compare against
     * @return list of recipes updated after the specified date
     */
    List<Recipe> findByUpdatedAtAfter(LocalDateTime date);

    /**
     * Find recipes created between two dates.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of recipes created within the specified date range
     */
    List<Recipe> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Check if a recipe with the given name exists.
     * 
     * @param name the name to check
     * @return true if a recipe with this name exists
     */
    boolean existsByName(String name);

    /**
     * Check if a recipe exists for a specific product.
     * 
     * @param product the product to check
     * @return true if a recipe exists for the product
     */
    boolean existsByProduct(Product product);

    /**
     * Find recipes that use a specific product as an ingredient.
     * 
     * @param ingredientProduct the product used as an ingredient
     * @return list of recipes that use the specified product
     */
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients ri WHERE ri.ingredientProduct = :ingredientProduct")
    List<Recipe> findRecipesUsingIngredient(@Param("ingredientProduct") Product ingredientProduct);

    /**
     * Find recipes ordered by name alphabetically.
     * 
     * @return list of recipes sorted by name
     */
    List<Recipe> findAllByOrderByNameAsc();

    /**
     * Find recipes ordered by creation date (newest first).
     * 
     * @return list of recipes sorted by creation date descending
     */
    List<Recipe> findAllByOrderByCreatedAtDesc();

    /**
     * Find recipes ordered by last update date (newest first).
     * 
     * @return list of recipes sorted by update date descending
     */
    List<Recipe> findAllByOrderByUpdatedAtDesc();

    /**
     * Search recipes by name or description containing text.
     * 
     * @param searchText the text to search for
     * @return list of recipes matching the search criteria
     */
    @Query("SELECT r FROM Recipe r WHERE " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Recipe> searchByNameOrDescription(@Param("searchText") String searchText);

    /**
     * Find recipes that have ingredients.
     * 
     * @return list of recipes that have at least one ingredient
     */
    @Query("SELECT DISTINCT r FROM Recipe r WHERE r.ingredients IS NOT EMPTY")
    List<Recipe> findRecipesWithIngredients();

    /**
     * Find recipes that have no ingredients.
     * 
     * @return list of recipes without any ingredients
     */
    @Query("SELECT r FROM Recipe r WHERE r.ingredients IS EMPTY")
    List<Recipe> findRecipesWithoutIngredients();
}
