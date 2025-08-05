package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing purchase transactions for acquiring products from suppliers.
 * Purchase records track incoming inventory, costs, supplier information,
 * and batch details for traceability and financial reporting.
 */
@Entity
@Table(name = "purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    /**
     * Unique identifier for the purchase transaction.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "purchase_id")
    private UUID purchaseId;

    /**
     * Quantity of products purchased in this transaction.
     * Required field representing the number of units acquired.
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Cost price per unit for this purchase.
     * Required field with precision of 10 digits and 2 decimal places.
     */
    @Column(name = "cost_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;

    /**
     * Batch number or lot number for product traceability.
     * Optional field with maximum length of 50 characters.
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    /**
     * Expiry date for perishable products.
     * Optional field for tracking product shelf life.
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    /**
     * Timestamp when the purchase transaction was recorded.
     * Automatically set to current timestamp on creation.
     */
    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    /**
     * Product being purchased in this transaction.
     * Required relationship - each purchase must be associated with a product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Supplier from whom the product is being purchased.
     * Required relationship - each purchase must be associated with a supplier.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    /**
     * Location where the purchased products will be stored.
     * Required relationship - each purchase must specify a destination location.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    /**
     * Automatically set purchase timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        purchaseDate = LocalDateTime.now();
    }
}
