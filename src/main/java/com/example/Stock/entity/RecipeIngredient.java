package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entity representing individual ingredients within a recipe.
 * Recipe ingredients define the specific products and quantities needed
 * to produce a finished product according to the recipe specifications.
 */
@Entity
@Table(name = "recipe_ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {

    /**
     * Unique identifier for the recipe ingredient.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "recipe_ingredient_id")
    private UUID recipeIngredientId;

    /**
     * Quantity of this ingredient required for the recipe.
     * Required field with precision of 10 digits and 3 decimal places
     * to accommodate fractional quantities.
     */
    @Column(name = "quantity", nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity;

    /**
     * Unit of measurement for this ingredient quantity.
     * Required field with maximum length of 20 characters
     * (e.g., "kg", "liters", "pieces", "cups").
     */
    @Column(name = "unit", nullable = false, length = 20)
    private String unit;

    /**
     * Cost per unit for this ingredient.
     * Optional field with precision of 10 digits and 2 decimal places
     * for calculating recipe costs.
     */
    @Column(name = "cost_per_unit", precision = 10, scale = 2)
    private BigDecimal costPerUnit;

    /**
     * Recipe to which this ingredient belongs.
     * Required relationship - each ingredient must be part of a recipe.
     * Uses CASCADE DELETE to remove ingredients when recipe is deleted.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    /**
     * Product that serves as the ingredient.
     * Required relationship - each ingredient must reference an existing product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_product_id", nullable = false)
    private Product ingredientProduct;

    /**
     * Calculate the total cost for this ingredient in the recipe.
     * 
     * @return the total cost (quantity * cost_per_unit), or null if cost_per_unit is not set
     */
    public BigDecimal getTotalCost() {
        if (costPerUnit == null || quantity == null) {
            return null;
        }
        return quantity.multiply(costPerUnit);
    }

    /**
     * Check if this ingredient has cost information available.
     * 
     * @return true if cost_per_unit is set, false otherwise
     */
    public boolean hasCostInfo() {
        return costPerUnit != null;
    }
}
