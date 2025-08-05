package com.example.Stock.entity;

/**
 * Enumeration representing different user roles in the stock management system.
 * Each role has different levels of access and permissions:
 * - ADMIN: Full system access and administrative privileges
 * - MANAGER: Management-level access for overseeing operations
 * - CASHIER: Basic access for sales and inventory operations
 */
public enum UserRole {
    /**
     * Administrator role with full system access.
     * Can manage users, configure system settings, and access all features.
     */
    ADMIN("admin"),

    /**
     * Manager role with supervisory access.
     * Can manage inventory, view reports, and oversee operations.
     */
    MANAGER("manager"),

    /**
     * Cashier role with basic operational access.
     * Can process sales, view inventory, and perform basic transactions.
     */
    CASHIER("cashier");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Convert string value to UserRole enum.
     * @param value the string value to convert
     * @return the corresponding UserRole enum
     * @throws IllegalArgumentException if the value doesn't match any role
     */
    public static UserRole fromValue(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid user role: " + value);
    }
}
