package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing current stock levels for products at specific locations.
 * This table maintains the current quantity of each product at each location
 * and serves as the primary source of truth for inventory levels.
 */
@Entity
@Table(name = "stock", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "location_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    /**
     * Unique identifier for the stock record.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "stock_id")
    private UUID stockId;

    /**
     * Current quantity of the product at this location.
     * Defaults to 0 if not specified.
     */
    @Column(name = "quantity")
    private Integer quantity = 0;

    /**
     * Timestamp when the stock level was last updated.
     * Automatically set to current timestamp on creation and updates.
     */
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    /**
     * Product for which this stock record maintains quantity.
     * Required relationship - each stock record must be associated with a product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Location where this stock is physically stored.
     * Required relationship - each stock record must be associated with a location.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    /**
     * Automatically set last updated timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        lastUpdated = LocalDateTime.now();
    }

    /**
     * Automatically update last updated timestamp before updating.
     */
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
