package com.example.Stock.repository;

import com.example.Stock.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Location entity operations.
 * Provides CRUD operations and custom queries for managing physical locations
 * where stock can be stored in the inventory management system.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    /**
     * Find a location by its name.
     * 
     * @param name the name of the location
     * @return Optional containing the location if found
     */
    Optional<Location> findByName(String name);

    /**
     * Find locations whose names contain the specified text (case-insensitive).
     * 
     * @param name the text to search for in location names
     * @return list of locations matching the search criteria
     */
    List<Location> findByNameContainingIgnoreCase(String name);

    /**
     * Check if a location with the given name exists.
     * 
     * @param name the name to check
     * @return true if a location with this name exists
     */
    boolean existsByName(String name);

    /**
     * Find all locations that have stock records.
     * 
     * @return list of locations that contain inventory
     */
    @Query("SELECT DISTINCT l FROM Location l JOIN l.stockRecords s WHERE s.quantity > 0")
    List<Location> findLocationsWithStock();

    /**
     * Find locations by address containing specific text.
     * 
     * @param address the text to search for in addresses
     * @return list of locations with matching addresses
     */
    List<Location> findByAddressContainingIgnoreCase(String address);
}
