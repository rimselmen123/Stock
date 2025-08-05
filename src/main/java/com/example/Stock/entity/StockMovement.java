package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing historical stock movements for audit and tracking purposes.
 * Stock movements provide a complete audit trail of all changes to inventory levels,
 * including the type of movement, quantity changes, and reference to the originating transaction.
 */
@Entity
@Table(name = "stock_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    /**
     * Unique identifier for the stock movement record.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "movement_id")
    private UUID movementId;

    /**
     * Change in quantity for this movement.
     * Positive values indicate stock increases, negative values indicate decreases.
     * Required field representing the actual quantity change.
     */
    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    /**
     * Type of movement that caused this stock change.
     * Required field indicating the business operation behind the movement.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 30)
    private MovementType movementType;

    /**
     * Reference ID to the originating transaction.
     * Optional field that can link to purchase_id, sale_id, transfer_id, etc.
     * depending on the movement type.
     */
    @Column(name = "reference_id")
    private UUID referenceId;

    /**
     * Timestamp when the movement was recorded.
     * Automatically set to current timestamp on creation.
     */
    @Column(name = "movement_date")
    private LocalDateTime movementDate;

    /**
     * Product for which this movement was recorded.
     * Required relationship - each movement must be associated with a product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Location where this stock movement occurred.
     * Required relationship - each movement must be associated with a location.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    /**
     * User who initiated or recorded this movement.
     * Optional relationship - some movements might be system-generated.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Automatically set movement timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        movementDate = LocalDateTime.now();
    }
}
