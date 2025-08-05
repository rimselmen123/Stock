package com.example.Stock.entity;

/**
 * Enumeration representing the status of inventory counting sessions.
 * Inventory sessions can be in one of two states:
 * - OPEN: Session is active and counting is in progress
 * - CLOSED: Session is completed and results have been finalized
 */
public enum InventoryStatus {
    /**
     * Inventory session is currently active and open for counting.
     * Products can be counted and recorded during this state.
     */
    OPEN("open"),

    /**
     * Inventory session has been completed and closed.
     * No further changes can be made to the counting results.
     */
    CLOSED("closed");

    private final String value;

    InventoryStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Convert string value to InventoryStatus enum.
     * @param value the string value to convert
     * @return the corresponding InventoryStatus enum
     * @throws IllegalArgumentException if the value doesn't match any status
     */
    public static InventoryStatus fromValue(String value) {
        for (InventoryStatus status : InventoryStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid inventory status: " + value);
    }
}
