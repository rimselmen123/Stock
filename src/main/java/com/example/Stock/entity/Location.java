package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing physical locations where stock can be stored.
 * Each location has a unique identifier, name, and optional address.
 * Locations are used to track where products are physically stored
 * and enable multi-location inventory management.
 */
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    /**
     * Unique identifier for the location.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "location_id")
    private UUID locationId;

    /**
     * Name of the location (e.g., "Main Warehouse", "Store Front").
     * Required field with maximum length of 100 characters.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Physical address of the location.
     * Optional field for storing detailed address information.
     */
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    /**
     * Stock records associated with this location.
     * One location can have multiple stock entries for different products.
     */
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stock> stockRecords;

    /**
     * Purchase records for products received at this location.
     * One location can have multiple purchase transactions.
     */
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases;

    /**
     * Sales records for products sold from this location.
     * One location can have multiple sales transactions.
     */
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> sales;

    /**
     * Transfer records where this location is the source (from_location).
     * One location can be the source of multiple transfer transactions.
     */
    @OneToMany(mappedBy = "fromLocation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transfer> outgoingTransfers;

    /**
     * Transfer records where this location is the destination (to_location).
     * One location can be the destination of multiple transfer transactions.
     */
    @OneToMany(mappedBy = "toLocation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transfer> incomingTransfers;

    /**
     * Stock movement records associated with this location.
     * One location can have multiple stock movement entries.
     */
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockMovement> stockMovements;

    /**
     * Inventory sessions conducted at this location.
     * One location can have multiple inventory counting sessions.
     */
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventorySession> inventorySessions;
}
