package com.example.Stock.repository;

import com.example.Stock.entity.Location;
import com.example.Stock.entity.Product;
import com.example.Stock.entity.Purchase;
import com.example.Stock.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Purchase entity operations.
 * Provides CRUD operations and custom queries for managing purchase transactions
 * in the inventory management system.
 */
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {

    /**
     * Find purchases by product.
     * 
     * @param product the product to search for
     * @return list of purchases for the specified product
     */
    List<Purchase> findByProduct(Product product);

    /**
     * Find purchases by supplier.
     * 
     * @param supplier the supplier to search for
     * @return list of purchases from the specified supplier
     */
    List<Purchase> findBySupplier(Supplier supplier);

    /**
     * Find purchases by location.
     * 
     * @param location the location to search for
     * @return list of purchases for the specified location
     */
    List<Purchase> findByLocation(Location location);

    /**
     * Find purchases by batch number.
     * 
     * @param batchNumber the batch number to search for
     * @return list of purchases with the specified batch number
     */
    List<Purchase> findByBatchNumber(String batchNumber);

    /**
     * Find purchases made after a specific date.
     * 
     * @param date the date to compare against
     * @return list of purchases made after the specified date
     */
    List<Purchase> findByPurchaseDateAfter(LocalDateTime date);

    /**
     * Find purchases made between two dates.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of purchases made within the specified date range
     */
    List<Purchase> findByPurchaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find purchases with expiry date before a specific date.
     * 
     * @param date the date to compare against
     * @return list of purchases expiring before the specified date
     */
    List<Purchase> findByExpiryDateBefore(LocalDate date);

    /**
     * Find purchases with expiry date between two dates.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of purchases with expiry dates within the range
     */
    List<Purchase> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find purchases with cost price greater than a threshold.
     * 
     * @param costPrice the minimum cost price
     * @return list of purchases above the cost threshold
     */
    List<Purchase> findByCostPriceGreaterThan(BigDecimal costPrice);

    /**
     * Calculate total purchase value for a specific period.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return total purchase value for the period
     */
    @Query("SELECT COALESCE(SUM(p.quantity * p.costPrice), 0) FROM Purchase p " +
           "WHERE p.purchaseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalPurchaseValue(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);

    /**
     * Calculate total purchase quantity for a product.
     * 
     * @param product the product to calculate total for
     * @return total quantity purchased for the product
     */
    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Purchase p WHERE p.product = :product")
    Long getTotalPurchaseQuantityForProduct(@Param("product") Product product);

    /**
     * Find recent purchases (ordered by purchase date, newest first).
     * 
     * @return list of recent purchases
     */
    List<Purchase> findAllByOrderByPurchaseDateDesc();

    /**
     * Find purchases by supplier and date range.
     * 
     * @param supplier the supplier
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of purchases from the supplier within the date range
     */
    List<Purchase> findBySupplierAndPurchaseDateBetween(Supplier supplier, 
                                                       LocalDateTime startDate, 
                                                       LocalDateTime endDate);

    /**
     * Count purchases by supplier.
     * 
     * @param supplier the supplier to count purchases for
     * @return number of purchases from the specified supplier
     */
    long countBySupplier(Supplier supplier);
}
