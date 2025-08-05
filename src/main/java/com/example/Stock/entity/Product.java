package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing products in the inventory management system.
 * Products are the core items being tracked, sold, and managed.
 * Each product can belong to a category and have multiple tags for flexible organization.
 */
@Entity
@Table(name = "products", uniqueConstraints = {
    @UniqueConstraint(columnNames = "barcode")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * Unique identifier for the product.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "product_id")
    private UUID productId;

    /**
     * Name of the product.
     * Required field with maximum length of 100 characters.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Unique barcode for product identification and scanning.
     * Optional field with maximum length of 50 characters.
     */
    @Column(name = "barcode", unique = true, length = 50)
    private String barcode;

    /**
     * Unit of measurement for the product (e.g., "pieces", "kg", "liters").
     * Optional field with maximum length of 20 characters.
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * Detailed description of the product.
     * Optional field for storing additional product information.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Category to which this product belongs.
     * Many products can belong to one category.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Tags associated with this product for flexible categorization.
     * Many-to-many relationship allowing multiple tags per product.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_tags",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    /**
     * Stock records for this product across different locations.
     * One product can have stock in multiple locations.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stock> stockRecords;

    /**
     * Purchase records for this product.
     * One product can have multiple purchase transactions.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases;

    /**
     * Sales records for this product.
     * One product can have multiple sales transactions.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> sales;

    /**
     * Transfer records for this product.
     * One product can be involved in multiple transfer transactions.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transfer> transfers;

    /**
     * Stock movement records for this product.
     * One product can have multiple stock movement entries.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockMovement> stockMovements;

    /**
     * Inventory line records for this product.
     * One product can appear in multiple inventory counting sessions.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventoryLine> inventoryLines;

    /**
     * Recipe associated with this product (if it's a finished product).
     * One product can have at most one recipe.
     */
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Recipe recipe;

    /**
     * Recipe ingredients where this product is used as an ingredient.
     * One product can be used as an ingredient in multiple recipes.
     */
    @OneToMany(mappedBy = "ingredientProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecipeIngredient> recipeIngredients;
}
