package com.example.Stock.repository;

import com.example.Stock.entity.Category;
import com.example.Stock.entity.Product;
import com.example.Stock.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Product entity operations.
 * Provides CRUD operations and custom queries for managing products
 * in the inventory management system.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Find a product by its name.
     * 
     * @param name the name of the product
     * @return Optional containing the product if found
     */
    Optional<Product> findByName(String name);

    /**
     * Find a product by its barcode.
     * 
     * @param barcode the barcode to search for
     * @return Optional containing the product if found
     */
    Optional<Product> findByBarcode(String barcode);

    /**
     * Find products whose names contain the specified text (case-insensitive).
     * 
     * @param name the text to search for in product names
     * @return list of products matching the search criteria
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Find products by category.
     * 
     * @param category the category to filter by
     * @return list of products in the specified category
     */
    List<Product> findByCategory(Category category);

    /**
     * Find products by category ID.
     * 
     * @param categoryId the category ID to filter by
     * @return list of products in the specified category
     */
    List<Product> findByCategoryCategoryId(UUID categoryId);

    /**
     * Find products that have a specific tag.
     * 
     * @param tag the tag to search for
     * @return list of products with the specified tag
     */
    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t = :tag")
    List<Product> findByTag(@Param("tag") Tag tag);

    /**
     * Find products by unit of measurement.
     * 
     * @param unit the unit to filter by
     * @return list of products with the specified unit
     */
    List<Product> findByUnit(String unit);

    /**
     * Check if a product with the given name exists.
     * 
     * @param name the name to check
     * @return true if a product with this name exists
     */
    boolean existsByName(String name);

    /**
     * Check if a product with the given barcode exists.
     * 
     * @param barcode the barcode to check
     * @return true if a product with this barcode exists
     */
    boolean existsByBarcode(String barcode);

    /**
     * Find products that have stock in any location.
     * 
     * @return list of products with stock records
     */
    @Query("SELECT DISTINCT p FROM Product p JOIN p.stockRecords s WHERE s.quantity > 0")
    List<Product> findProductsWithStock();

    /**
     * Find products that have no stock in any location.
     * 
     * @return list of products without stock
     */
    @Query("SELECT p FROM Product p WHERE p.stockRecords IS EMPTY OR " +
           "NOT EXISTS (SELECT s FROM Stock s WHERE s.product = p AND s.quantity > 0)")
    List<Product> findProductsWithoutStock();

    /**
     * Find products ordered by name alphabetically.
     * 
     * @return list of products sorted by name
     */
    List<Product> findAllByOrderByNameAsc();

    /**
     * Search products by name or description containing text.
     * 
     * @param searchText the text to search for
     * @return list of products matching the search criteria
     */
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Product> searchByNameOrDescription(@Param("searchText") String searchText);
}
