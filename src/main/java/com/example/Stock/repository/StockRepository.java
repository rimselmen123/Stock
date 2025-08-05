package com.example.Stock.repository;

import com.example.Stock.entity.Location;
import com.example.Stock.entity.Product;
import com.example.Stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Stock entity operations.
 * Provides CRUD operations and custom queries for managing stock levels
 * in the inventory management system.
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    /**
     * Find stock record for a specific product at a specific location.
     * 
     * @param product the product to search for
     * @param location the location to search for
     * @return Optional containing the stock record if found
     */
    Optional<Stock> findByProductAndLocation(Product product, Location location);

    /**
     * Find stock record by product ID and location ID.
     * 
     * @param productId the product ID
     * @param locationId the location ID
     * @return Optional containing the stock record if found
     */
    Optional<Stock> findByProductProductIdAndLocationLocationId(UUID productId, UUID locationId);

    /**
     * Find all stock records for a specific product.
     * 
     * @param product the product to search for
     * @return list of stock records for the product
     */
    List<Stock> findByProduct(Product product);

    /**
     * Find all stock records for a specific location.
     * 
     * @param location the location to search for
     * @return list of stock records for the location
     */
    List<Stock> findByLocation(Location location);

    /**
     * Find stock records with quantity greater than zero.
     * 
     * @return list of stock records with positive quantities
     */
    List<Stock> findByQuantityGreaterThan(Integer quantity);

    /**
     * Find stock records with zero or negative quantities.
     * 
     * @return list of stock records with zero or negative quantities
     */
    List<Stock> findByQuantityLessThanEqual(Integer quantity);

    /**
     * Find low stock items (quantity below threshold).
     * 
     * @param threshold the minimum quantity threshold
     * @return list of stock records below the threshold
     */
    @Query("SELECT s FROM Stock s WHERE s.quantity <= :threshold AND s.quantity >= 0")
    List<Stock> findLowStockItems(@Param("threshold") Integer threshold);

    /**
     * Find out of stock items (quantity is zero).
     * 
     * @return list of stock records with zero quantity
     */
    List<Stock> findByQuantity(Integer quantity);

    /**
     * Calculate total stock quantity for a product across all locations.
     * 
     * @param product the product to calculate total stock for
     * @return total quantity across all locations
     */
    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Stock s WHERE s.product = :product")
    Long getTotalStockForProduct(@Param("product") Product product);

    /**
     * Calculate total stock quantity for a product by product ID.
     * 
     * @param productId the product ID
     * @return total quantity across all locations
     */
    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Stock s WHERE s.product.productId = :productId")
    Long getTotalStockForProductById(@Param("productId") UUID productId);

    /**
     * Find stock records for products in a specific category.
     * 
     * @param categoryId the category ID
     * @return list of stock records for products in the category
     */
    @Query("SELECT s FROM Stock s WHERE s.product.category.categoryId = :categoryId")
    List<Stock> findByProductCategory(@Param("categoryId") UUID categoryId);

    /**
     * Find stock records ordered by quantity (lowest first).
     * 
     * @return list of stock records sorted by quantity ascending
     */
    List<Stock> findAllByOrderByQuantityAsc();

    /**
     * Find stock records ordered by last updated date (newest first).
     * 
     * @return list of stock records sorted by last updated descending
     */
    List<Stock> findAllByOrderByLastUpdatedDesc();

    /**
     * Check if stock exists for a product at a location.
     * 
     * @param product the product
     * @param location the location
     * @return true if stock record exists
     */
    boolean existsByProductAndLocation(Product product, Location location);
}
