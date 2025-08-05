package com.example.Stock.repository;

import com.example.Stock.entity.Location;
import com.example.Stock.entity.MovementType;
import com.example.Stock.entity.Product;
import com.example.Stock.entity.StockMovement;
import com.example.Stock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for StockMovement entity operations.
 * Provides CRUD operations and custom queries for managing stock movement records
 * in the inventory management system.
 */
@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {

    /**
     * Find stock movements by product.
     * 
     * @param product the product to search for
     * @return list of stock movements for the specified product
     */
    List<StockMovement> findByProduct(Product product);

    /**
     * Find stock movements by location.
     * 
     * @param location the location to search for
     * @return list of stock movements for the specified location
     */
    List<StockMovement> findByLocation(Location location);

    /**
     * Find stock movements by movement type.
     * 
     * @param movementType the movement type to filter by
     * @return list of stock movements of the specified type
     */
    List<StockMovement> findByMovementType(MovementType movementType);

    /**
     * Find stock movements by user.
     * 
     * @param user the user to search for
     * @return list of stock movements recorded by the specified user
     */
    List<StockMovement> findByUser(User user);

    /**
     * Find stock movements by reference ID.
     * 
     * @param referenceId the reference ID to search for
     * @return list of stock movements with the specified reference ID
     */
    List<StockMovement> findByReferenceId(UUID referenceId);

    /**
     * Find stock movements after a specific date.
     * 
     * @param date the date to compare against
     * @return list of stock movements after the specified date
     */
    List<StockMovement> findByMovementDateAfter(LocalDateTime date);

    /**
     * Find stock movements between two dates.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of stock movements within the specified date range
     */
    List<StockMovement> findByMovementDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find stock movements with positive quantity changes (increases).
     * 
     * @return list of stock movements that increased inventory
     */
    List<StockMovement> findByQuantityChangeGreaterThan(Integer quantityChange);

    /**
     * Find stock movements with negative quantity changes (decreases).
     * 
     * @return list of stock movements that decreased inventory
     */
    List<StockMovement> findByQuantityChangeLessThan(Integer quantityChange);

    /**
     * Calculate total quantity change for a product.
     * 
     * @param product the product to calculate total for
     * @return total quantity change for the product
     */
    @Query("SELECT COALESCE(SUM(sm.quantityChange), 0) FROM StockMovement sm WHERE sm.product = :product")
    Long getTotalQuantityChangeForProduct(@Param("product") Product product);

    /**
     * Calculate total quantity change for a product at a specific location.
     * 
     * @param product the product
     * @param location the location
     * @return total quantity change for the product at the location
     */
    @Query("SELECT COALESCE(SUM(sm.quantityChange), 0) FROM StockMovement sm " +
           "WHERE sm.product = :product AND sm.location = :location")
    Long getTotalQuantityChangeForProductAtLocation(@Param("product") Product product, 
                                                   @Param("location") Location location);

    /**
     * Find recent stock movements (ordered by movement date, newest first).
     * 
     * @return list of recent stock movements
     */
    List<StockMovement> findAllByOrderByMovementDateDesc();

    /**
     * Find stock movements by product and movement type.
     * 
     * @param product the product
     * @param movementType the movement type
     * @return list of stock movements matching both criteria
     */
    List<StockMovement> findByProductAndMovementType(Product product, MovementType movementType);

    /**
     * Find stock movements by location and movement type.
     * 
     * @param location the location
     * @param movementType the movement type
     * @return list of stock movements matching both criteria
     */
    List<StockMovement> findByLocationAndMovementType(Location location, MovementType movementType);

    /**
     * Find stock movements by user and date range.
     * 
     * @param user the user
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of stock movements by the user within the date range
     */
    List<StockMovement> findByUserAndMovementDateBetween(User user, 
                                                        LocalDateTime startDate, 
                                                        LocalDateTime endDate);

    /**
     * Count stock movements by movement type.
     * 
     * @param movementType the movement type to count
     * @return number of stock movements of the specified type
     */
    long countByMovementType(MovementType movementType);

    /**
     * Find stock movements for audit trail of a specific transaction.
     * 
     * @param referenceId the reference ID of the transaction
     * @param movementType the expected movement type
     * @return list of stock movements for the transaction
     */
    List<StockMovement> findByReferenceIdAndMovementType(UUID referenceId, MovementType movementType);
}
