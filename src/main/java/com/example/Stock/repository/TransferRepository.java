package com.example.Stock.repository;

import com.example.Stock.entity.Location;
import com.example.Stock.entity.Product;
import com.example.Stock.entity.Transfer;
import com.example.Stock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Transfer entity operations.
 * Provides CRUD operations and custom queries for managing transfer transactions
 * in the inventory management system.
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    /**
     * Find transfers by product.
     * 
     * @param product the product to search for
     * @return list of transfers for the specified product
     */
    List<Transfer> findByProduct(Product product);

    /**
     * Find transfers from a specific location.
     * 
     * @param fromLocation the source location
     * @return list of transfers from the specified location
     */
    List<Transfer> findByFromLocation(Location fromLocation);

    /**
     * Find transfers to a specific location.
     * 
     * @param toLocation the destination location
     * @return list of transfers to the specified location
     */
    List<Transfer> findByToLocation(Location toLocation);

    /**
     * Find transfers initiated by a specific user.
     * 
     * @param user the user who initiated the transfers
     * @return list of transfers initiated by the specified user
     */
    List<Transfer> findByUser(User user);

    /**
     * Find transfers made after a specific date.
     * 
     * @param date the date to compare against
     * @return list of transfers made after the specified date
     */
    List<Transfer> findByTransferDateAfter(LocalDateTime date);

    /**
     * Find transfers made between two dates.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of transfers made within the specified date range
     */
    List<Transfer> findByTransferDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find transfers between two specific locations.
     * 
     * @param fromLocation the source location
     * @param toLocation the destination location
     * @return list of transfers between the specified locations
     */
    List<Transfer> findByFromLocationAndToLocation(Location fromLocation, Location toLocation);

    /**
     * Find transfers involving a specific location (either as source or destination).
     * 
     * @param location the location to search for
     * @return list of transfers involving the specified location
     */
    @Query("SELECT t FROM Transfer t WHERE t.fromLocation = :location OR t.toLocation = :location")
    List<Transfer> findByLocation(@Param("location") Location location);

    /**
     * Calculate total quantity transferred for a product.
     * 
     * @param product the product to calculate total for
     * @return total quantity transferred for the product
     */
    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM Transfer t WHERE t.product = :product")
    Long getTotalTransferQuantityForProduct(@Param("product") Product product);

    /**
     * Calculate total quantity transferred from a location.
     * 
     * @param location the source location
     * @return total quantity transferred from the location
     */
    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM Transfer t WHERE t.fromLocation = :location")
    Long getTotalOutgoingTransferQuantity(@Param("location") Location location);

    /**
     * Calculate total quantity transferred to a location.
     * 
     * @param location the destination location
     * @return total quantity transferred to the location
     */
    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM Transfer t WHERE t.toLocation = :location")
    Long getTotalIncomingTransferQuantity(@Param("location") Location location);

    /**
     * Find recent transfers (ordered by transfer date, newest first).
     * 
     * @return list of recent transfers
     */
    List<Transfer> findAllByOrderByTransferDateDesc();

    /**
     * Find transfers by user and date range.
     * 
     * @param user the user
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of transfers by the user within the date range
     */
    List<Transfer> findByUserAndTransferDateBetween(User user, 
                                                   LocalDateTime startDate, 
                                                   LocalDateTime endDate);

    /**
     * Count transfers by user.
     * 
     * @param user the user to count transfers for
     * @return number of transfers initiated by the specified user
     */
    long countByUser(User user);

    /**
     * Find transfers for a product between specific locations and date range.
     * 
     * @param product the product
     * @param fromLocation the source location
     * @param toLocation the destination location
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of transfers matching all criteria
     */
    List<Transfer> findByProductAndFromLocationAndToLocationAndTransferDateBetween(
        Product product, 
        Location fromLocation, 
        Location toLocation, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
}
