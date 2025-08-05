package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing transfer transactions for moving products between locations.
 * Transfer records track the movement of inventory from one location to another,
 * maintaining audit trails for stock movements across the organization.
 */
@Entity
@Table(name = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    /**
     * Unique identifier for the transfer transaction.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "transfer_id")
    private UUID transferId;

    /**
     * Quantity of products being transferred.
     * Required field representing the number of units moved.
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Timestamp when the transfer transaction was completed.
     * Automatically set to current timestamp on creation.
     */
    @Column(name = "transfer_date")
    private LocalDateTime transferDate;

    /**
     * Product being transferred between locations.
     * Required relationship - each transfer must be associated with a product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Source location from which the product is being transferred.
     * Required relationship - each transfer must specify a source location.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location", nullable = false)
    private Location fromLocation;

    /**
     * Destination location to which the product is being transferred.
     * Required relationship - each transfer must specify a destination location.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location", nullable = false)
    private Location toLocation;

    /**
     * User who initiated this transfer transaction.
     * Required relationship - each transfer must be associated with a user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Automatically set transfer timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        transferDate = LocalDateTime.now();
    }
}
