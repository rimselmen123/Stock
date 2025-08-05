package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing recipes for finished products that are assembled from ingredients.
 * Recipes define the bill of materials (BOM) for products that are manufactured
 * or assembled from other products in the inventory system.
 */
@Entity
@Table(name = "recipes", uniqueConstraints = {
    @UniqueConstraint(columnNames = "product_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    /**
     * Unique identifier for the recipe.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "recipe_id")
    private UUID recipeId;

    /**
     * Name of the recipe.
     * Required field with maximum length of 150 characters.
     */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /**
     * Detailed description of the recipe.
     * Optional field for storing preparation instructions, notes, etc.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Timestamp when the recipe was created.
     * Automatically set to current timestamp on creation.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the recipe was last updated.
     * Automatically updated on each modification.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Finished product that this recipe produces.
     * Required one-to-one relationship - each recipe produces exactly one product.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    /**
     * List of ingredients required for this recipe.
     * One recipe can have multiple ingredients with specific quantities.
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecipeIngredient> ingredients;

    /**
     * Automatically set creation timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Automatically update timestamp before updating.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
