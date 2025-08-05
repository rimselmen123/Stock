package com.example.Stock.repository;

import com.example.Stock.entity.Product;
import com.example.Stock.entity.Recipe;
import com.example.Stock.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for RecipeIngredient entity operations.
 * Provides CRUD operations and custom queries for managing recipe ingredients
 * in the inventory management system.
 */
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, UUID> {

    /**
     * Find recipe ingredients by recipe.
     * 
     * @param recipe the recipe to search for
     * @return list of ingredients for the specified recipe
     */
    List<RecipeIngredient> findByRecipe(Recipe recipe);

    /**
     * Find recipe ingredients by ingredient product.
     * 
     * @param ingredientProduct the product used as an ingredient
     * @return list of recipe ingredients using the specified product
     */
    List<RecipeIngredient> findByIngredientProduct(Product ingredientProduct);

    /**
     * Find recipe ingredient for a specific product in a specific recipe.
     * 
     * @param recipe the recipe
     * @param ingredientProduct the ingredient product
     * @return Optional containing the recipe ingredient if found
     */
    Optional<RecipeIngredient> findByRecipeAndIngredientProduct(Recipe recipe, Product ingredientProduct);

    /**
     * Find recipe ingredients by unit of measurement.
     * 
     * @param unit the unit to filter by
     * @return list of recipe ingredients with the specified unit
     */
    List<RecipeIngredient> findByUnit(String unit);

    /**
     * Find recipe ingredients with cost information.
     * 
     * @param recipe the recipe
     * @return list of ingredients with cost per unit set
     */
    List<RecipeIngredient> findByRecipeAndCostPerUnitIsNotNull(Recipe recipe);

    /**
     * Find recipe ingredients without cost information.
     * 
     * @param recipe the recipe
     * @return list of ingredients without cost per unit
     */
    List<RecipeIngredient> findByRecipeAndCostPerUnitIsNull(Recipe recipe);

    /**
     * Find recipe ingredients with quantity greater than a threshold.
     * 
     * @param quantity the minimum quantity
     * @return list of ingredients with quantity above the threshold
     */
    List<RecipeIngredient> findByQuantityGreaterThan(BigDecimal quantity);

    /**
     * Find recipe ingredients with cost per unit greater than a threshold.
     * 
     * @param costPerUnit the minimum cost per unit
     * @return list of ingredients with cost above the threshold
     */
    List<RecipeIngredient> findByCostPerUnitGreaterThan(BigDecimal costPerUnit);

    /**
     * Calculate total cost for a recipe.
     * 
     * @param recipe the recipe
     * @return total cost of all ingredients with cost information
     */
    @Query("SELECT COALESCE(SUM(ri.quantity * ri.costPerUnit), 0) FROM RecipeIngredient ri " +
           "WHERE ri.recipe = :recipe AND ri.costPerUnit IS NOT NULL")
    BigDecimal getTotalCostForRecipe(@Param("recipe") Recipe recipe);

    /**
     * Calculate total quantity of a specific ingredient across all recipes.
     * 
     * @param ingredientProduct the ingredient product
     * @return total quantity used across all recipes
     */
    @Query("SELECT COALESCE(SUM(ri.quantity), 0) FROM RecipeIngredient ri " +
           "WHERE ri.ingredientProduct = :ingredientProduct")
    BigDecimal getTotalUsageForIngredient(@Param("ingredientProduct") Product ingredientProduct);

    /**
     * Count ingredients in a recipe.
     * 
     * @param recipe the recipe
     * @return number of ingredients in the recipe
     */
    long countByRecipe(Recipe recipe);

    /**
     * Count recipes that use a specific ingredient.
     * 
     * @param ingredientProduct the ingredient product
     * @return number of recipes using the ingredient
     */
    long countByIngredientProduct(Product ingredientProduct);

    /**
     * Find recipes that use the most of a specific ingredient.
     * 
     * @param ingredientProduct the ingredient product
     * @return list of recipe ingredients ordered by quantity descending
     */
    List<RecipeIngredient> findByIngredientProductOrderByQuantityDesc(Product ingredientProduct);

    /**
     * Find most expensive ingredients in a recipe.
     * 
     * @param recipe the recipe
     * @return list of ingredients ordered by total cost descending
     */
    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.recipe = :recipe " +
           "AND ri.costPerUnit IS NOT NULL ORDER BY (ri.quantity * ri.costPerUnit) DESC")
    List<RecipeIngredient> findMostExpensiveIngredientsInRecipe(@Param("recipe") Recipe recipe);

    /**
     * Find ingredients ordered by quantity (largest first).
     * 
     * @param recipe the recipe
     * @return list of ingredients sorted by quantity descending
     */
    List<RecipeIngredient> findByRecipeOrderByQuantityDesc(Recipe recipe);

    /**
     * Check if a recipe uses a specific ingredient.
     * 
     * @param recipe the recipe
     * @param ingredientProduct the ingredient product
     * @return true if the recipe uses the ingredient
     */
    boolean existsByRecipeAndIngredientProduct(Recipe recipe, Product ingredientProduct);

    /**
     * Find all unique units used in recipe ingredients.
     * 
     * @return list of distinct units
     */
    @Query("SELECT DISTINCT ri.unit FROM RecipeIngredient ri ORDER BY ri.unit")
    List<String> findDistinctUnits();
}
