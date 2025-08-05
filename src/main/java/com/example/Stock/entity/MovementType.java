package com.example.Stock.entity;

/**
 * Enumeration representing different types of stock movements in the inventory system.
 * Each movement type corresponds to a specific business operation that affects stock levels:
 * - PURCHASE: Stock increase due to purchasing from suppliers
 * - SALE: Stock decrease due to sales to customers
 * - TRANSFER_IN: Stock increase due to incoming transfers from other locations
 * - TRANSFER_OUT: Stock decrease due to outgoing transfers to other locations
 * - INVENTORY_ADJUSTMENT: Stock changes due to inventory corrections or adjustments
 */
public enum MovementType {
    /**
     * Stock movement resulting from purchasing products from suppliers.
     * Increases stock levels at the destination location.
     */
    PURCHASE("purchase"),

    /**
     * Stock movement resulting from selling products to customers.
     * Decreases stock levels at the source location.
     */
    SALE("sale"),

    /**
     * Stock movement resulting from receiving products via transfer from another location.
     * Increases stock levels at the destination location.
     */
    TRANSFER_IN("transfer_in"),

    /**
     * Stock movement resulting from sending products via transfer to another location.
     * Decreases stock levels at the source location.
     */
    TRANSFER_OUT("transfer_out"),

    /**
     * Stock movement resulting from inventory adjustments or corrections.
     * Can either increase or decrease stock levels based on actual vs. recorded quantities.
     */
    INVENTORY_ADJUSTMENT("inventory_adjustment");

    private final String value;

    MovementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Convert string value to MovementType enum.
     * @param value the string value to convert
     * @return the corresponding MovementType enum
     * @throws IllegalArgumentException if the value doesn't match any movement type
     */
    public static MovementType fromValue(String value) {
        for (MovementType type : MovementType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid movement type: " + value);
    }
}
