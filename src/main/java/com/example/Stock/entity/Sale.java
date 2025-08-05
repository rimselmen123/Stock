package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing sales transactions for products sold to customers.
 * Sale records track outgoing inventory, revenue, location, and the user
 * who processed the transaction for reporting and audit purposes.
 */
@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    /**
     * Unique identifier for the sales transaction.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "sale_id")
    private UUID saleId;

    /**
     * Quantity of products sold in this transaction.
     * Required field representing the number of units sold.
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Sale price per unit for this transaction.
     * Required field with precision of 10 digits and 2 decimal places.
     */
    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    /**
     * Timestamp when the sales transaction was completed.
     * Automatically set to current timestamp on creation.
     */
    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    /**
     * Product being sold in this transaction.
     * Required relationship - each sale must be associated with a product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Location from which the product is being sold.
     * Required relationship - each sale must specify a source location.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    /**
     * User who processed this sales transaction.
     * Required relationship - each sale must be associated with a user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Automatically set sale timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        saleDate = LocalDateTime.now();
    }
}
