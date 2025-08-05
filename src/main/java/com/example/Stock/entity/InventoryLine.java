package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

/**
 * Entity representing individual product counts within an inventory session.
 * Inventory lines capture the expected vs. actual quantities for each product
 * during physical stock counting, automatically calculating discrepancies.
 */
@Entity
@Table(name = "inventory_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryLine {

    /**
     * Unique identifier for the inventory line.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "line_id")
    private UUID lineId;

    /**
     * Expected quantity based on system records.
     * This represents what the system believes should be in stock.
     */
    @Column(name = "expected_quantity")
    private Integer expectedQuantity;

    /**
     * Actual quantity counted during the physical inventory.
     * This represents what was physically found during counting.
     */
    @Column(name = "counted_quantity")
    private Integer countedQuantity;

    /**
     * Additional notes or comments about this inventory line.
     * Optional field for recording observations, explanations for discrepancies, etc.
     */
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    /**
     * Inventory session to which this line belongs.
     * Required relationship - each line must be part of a session.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InventorySession session;

    /**
     * Product being counted in this inventory line.
     * Required relationship - each line must be associated with a product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Calculate the difference between counted and expected quantities.
     * This method provides the same functionality as the database generated column.
     * Positive values indicate surplus, negative values indicate shortage.
     * 
     * @return the difference (counted_quantity - expected_quantity)
     */
    public Integer getDifference() {
        if (countedQuantity == null || expectedQuantity == null) {
            return null;
        }
        return countedQuantity - expectedQuantity;
    }

    /**
     * Check if there is a discrepancy between expected and counted quantities.
     * 
     * @return true if there is a difference, false if quantities match
     */
    public boolean hasDiscrepancy() {
        Integer difference = getDifference();
        return difference != null && difference != 0;
    }

    /**
     * Check if this line represents a surplus (more than expected).
     * 
     * @return true if counted quantity exceeds expected quantity
     */
    public boolean isSurplus() {
        Integer difference = getDifference();
        return difference != null && difference > 0;
    }

    /**
     * Check if this line represents a shortage (less than expected).
     * 
     * @return true if counted quantity is less than expected quantity
     */
    public boolean isShortage() {
        Integer difference = getDifference();
        return difference != null && difference < 0;
    }
}
