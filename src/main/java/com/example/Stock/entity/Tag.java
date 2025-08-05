package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing tags that can be associated with products for flexible categorization.
 * Tags provide an additional layer of product organization beyond categories,
 * allowing for multiple labels per product (e.g., "organic", "seasonal", "bestseller").
 */
@Entity
@Table(name = "tags", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    /**
     * Unique identifier for the tag.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "tag_id")
    private UUID tagId;

    /**
     * Name of the tag (e.g., "organic", "seasonal", "bestseller").
     * Required and unique field with maximum length of 50 characters.
     */
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Products associated with this tag through many-to-many relationship.
     * One tag can be applied to multiple products, and one product can have multiple tags.
     */
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Product> products;
}
