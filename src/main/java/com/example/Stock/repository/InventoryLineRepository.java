package com.example.Stock.repository;

import com.example.Stock.entity.InventoryLine;
import com.example.Stock.entity.InventorySession;
import com.example.Stock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for InventoryLine entity operations.
 * Provides CRUD operations and custom queries for managing inventory line records
 * in the inventory management system.
 */
@Repository
public interface InventoryLineRepository extends JpaRepository<InventoryLine, UUID> {

    /**
     * Find inventory lines by session.
     * 
     * @param session the inventory session
     * @return list of inventory lines for the specified session
     */
    List<InventoryLine> findBySession(InventorySession session);

    /**
     * Find inventory lines by product.
     * 
     * @param product the product to search for
     * @return list of inventory lines for the specified product
     */
    List<InventoryLine> findByProduct(Product product);

    /**
     * Find inventory line for a specific product in a specific session.
     * 
     * @param session the inventory session
     * @param product the product
     * @return Optional containing the inventory line if found
     */
    Optional<InventoryLine> findBySessionAndProduct(InventorySession session, Product product);

    /**
     * Find inventory lines with discrepancies (counted != expected).
     * 
     * @param session the inventory session
     * @return list of inventory lines with discrepancies
     */
    @Query("SELECT il FROM InventoryLine il WHERE il.session = :session " +
           "AND il.countedQuantity != il.expectedQuantity")
    List<InventoryLine> findDiscrepanciesInSession(@Param("session") InventorySession session);

    /**
     * Find inventory lines with surplus (counted > expected).
     * 
     * @param session the inventory session
     * @return list of inventory lines with surplus
     */
    @Query("SELECT il FROM InventoryLine il WHERE il.session = :session " +
           "AND il.countedQuantity > il.expectedQuantity")
    List<InventoryLine> findSurplusInSession(@Param("session") InventorySession session);

    /**
     * Find inventory lines with shortage (counted < expected).
     * 
     * @param session the inventory session
     * @return list of inventory lines with shortage
     */
    @Query("SELECT il FROM InventoryLine il WHERE il.session = :session " +
           "AND il.countedQuantity < il.expectedQuantity")
    List<InventoryLine> findShortageInSession(@Param("session") InventorySession session);

    /**
     * Find inventory lines where counting is not yet completed.
     * 
     * @param session the inventory session
     * @return list of inventory lines with null counted quantity
     */
    List<InventoryLine> findBySessionAndCountedQuantityIsNull(InventorySession session);

    /**
     * Find inventory lines where counting is completed.
     * 
     * @param session the inventory session
     * @return list of inventory lines with non-null counted quantity
     */
    List<InventoryLine> findBySessionAndCountedQuantityIsNotNull(InventorySession session);

    /**
     * Calculate total expected quantity for a session.
     * 
     * @param session the inventory session
     * @return total expected quantity
     */
    @Query("SELECT COALESCE(SUM(il.expectedQuantity), 0) FROM InventoryLine il WHERE il.session = :session")
    Long getTotalExpectedQuantityForSession(@Param("session") InventorySession session);

    /**
     * Calculate total counted quantity for a session.
     * 
     * @param session the inventory session
     * @return total counted quantity
     */
    @Query("SELECT COALESCE(SUM(il.countedQuantity), 0) FROM InventoryLine il " +
           "WHERE il.session = :session AND il.countedQuantity IS NOT NULL")
    Long getTotalCountedQuantityForSession(@Param("session") InventorySession session);

    /**
     * Calculate total discrepancy for a session.
     * 
     * @param session the inventory session
     * @return total discrepancy (positive = surplus, negative = shortage)
     */
    @Query("SELECT COALESCE(SUM(il.countedQuantity - il.expectedQuantity), 0) FROM InventoryLine il " +
           "WHERE il.session = :session AND il.countedQuantity IS NOT NULL")
    Long getTotalDiscrepancyForSession(@Param("session") InventorySession session);

    /**
     * Count inventory lines with discrepancies in a session.
     * 
     * @param session the inventory session
     * @return number of lines with discrepancies
     */
    @Query("SELECT COUNT(il) FROM InventoryLine il WHERE il.session = :session " +
           "AND il.countedQuantity != il.expectedQuantity")
    long countDiscrepanciesInSession(@Param("session") InventorySession session);

    /**
     * Count completed inventory lines in a session.
     * 
     * @param session the inventory session
     * @return number of lines with counted quantities
     */
    long countBySessionAndCountedQuantityIsNotNull(InventorySession session);

    /**
     * Count pending inventory lines in a session.
     * 
     * @param session the inventory session
     * @return number of lines without counted quantities
     */
    long countBySessionAndCountedQuantityIsNull(InventorySession session);

    /**
     * Find inventory lines with notes.
     * 
     * @param session the inventory session
     * @return list of inventory lines that have notes
     */
    @Query("SELECT il FROM InventoryLine il WHERE il.session = :session AND il.note IS NOT NULL")
    List<InventoryLine> findLinesWithNotesInSession(@Param("session") InventorySession session);

    /**
     * Check if a product has been counted in a session.
     * 
     * @param session the inventory session
     * @param product the product
     * @return true if the product has been counted
     */
    @Query("SELECT COUNT(il) > 0 FROM InventoryLine il WHERE il.session = :session " +
           "AND il.product = :product AND il.countedQuantity IS NOT NULL")
    boolean isProductCountedInSession(@Param("session") InventorySession session, 
                                    @Param("product") Product product);
}
