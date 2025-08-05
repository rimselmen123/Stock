package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing product categories for organizing and classifying products.
 * Categories help in grouping similar products together for better inventory management,
 * reporting, and product organization.
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /**
     * Unique identifier for the category.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "category_id")
    private UUID categoryId;

    /**
     * Name of the category (e.g., "Electronics", "Food & Beverages", "Clothing").
     * Required field with maximum length of 100 characters.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Products that belong to this category.
     * One category can contain multiple products.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}
