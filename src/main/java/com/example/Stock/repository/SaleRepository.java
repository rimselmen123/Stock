package com.example.Stock.repository;

import com.example.Stock.entity.Location;
import com.example.Stock.entity.Product;
import com.example.Stock.entity.Sale;
import com.example.Stock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Sale entity operations.
 * Provides CRUD operations and custom queries for managing sales transactions
 * in the inventory management system.
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {

    /**
     * Find sales by product.
     * 
     * @param product the product to search for
     * @return list of sales for the specified product
     */
    List<Sale> findByProduct(Product product);

    /**
     * Find sales by location.
     * 
     * @param location the location to search for
     * @return list of sales from the specified location
     */
    List<Sale> findByLocation(Location location);

    /**
     * Find sales by user.
     * 
     * @param user the user to search for
     * @return list of sales processed by the specified user
     */
    List<Sale> findByUser(User user);

    /**
     * Find sales made after a specific date.
     * 
     * @param date the date to compare against
     * @return list of sales made after the specified date
     */
    List<Sale> findBySaleDateAfter(LocalDateTime date);

    /**
     * Find sales made between two dates.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of sales made within the specified date range
     */
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find sales with sale price greater than a threshold.
     * 
     * @param salePrice the minimum sale price
     * @return list of sales above the price threshold
     */
    List<Sale> findBySalePriceGreaterThan(BigDecimal salePrice);

    /**
     * Calculate total sales revenue for a specific period.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return total sales revenue for the period
     */
    @Query("SELECT COALESCE(SUM(s.quantity * s.salePrice), 0) FROM Sale s " +
           "WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSalesRevenue(@Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);

    /**
     * Calculate total sales quantity for a product.
     * 
     * @param product the product to calculate total for
     * @return total quantity sold for the product
     */
    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Sale s WHERE s.product = :product")
    Long getTotalSalesQuantityForProduct(@Param("product") Product product);

    /**
     * Calculate total sales revenue for a product.
     * 
     * @param product the product to calculate revenue for
     * @return total revenue from the product
     */
    @Query("SELECT COALESCE(SUM(s.quantity * s.salePrice), 0) FROM Sale s WHERE s.product = :product")
    BigDecimal getTotalRevenueForProduct(@Param("product") Product product);

    /**
     * Find recent sales (ordered by sale date, newest first).
     * 
     * @return list of recent sales
     */
    List<Sale> findAllByOrderBySaleDateDesc();

    /**
     * Find sales by user and date range.
     * 
     * @param user the user
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of sales by the user within the date range
     */
    List<Sale> findByUserAndSaleDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find sales by location and date range.
     * 
     * @param location the location
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of sales from the location within the date range
     */
    List<Sale> findByLocationAndSaleDateBetween(Location location, 
                                              LocalDateTime startDate, 
                                              LocalDateTime endDate);

    /**
     * Count sales by user.
     * 
     * @param user the user to count sales for
     * @return number of sales processed by the specified user
     */
    long countByUser(User user);

    /**
     * Find top selling products by quantity.
     * 
     * @param limit the maximum number of products to return
     * @return list of products with their total sales quantities
     */
    @Query("SELECT s.product, SUM(s.quantity) as totalQuantity FROM Sale s " +
           "GROUP BY s.product ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProductsByQuantity();

    /**
     * Find top selling products by revenue.
     * 
     * @param limit the maximum number of products to return
     * @return list of products with their total sales revenue
     */
    @Query("SELECT s.product, SUM(s.quantity * s.salePrice) as totalRevenue FROM Sale s " +
           "GROUP BY s.product ORDER BY totalRevenue DESC")
    List<Object[]> findTopSellingProductsByRevenue();
}
