package com.example.Stock.repository;

import com.example.Stock.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Supplier entity operations.
 * Provides CRUD operations and custom queries for managing suppliers
 * in the inventory management system.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    /**
     * Find a supplier by its name.
     * 
     * @param name the name of the supplier
     * @return Optional containing the supplier if found
     */
    Optional<Supplier> findByName(String name);

    /**
     * Find suppliers whose names contain the specified text (case-insensitive).
     * 
     * @param name the text to search for in supplier names
     * @return list of suppliers matching the search criteria
     */
    List<Supplier> findByNameContainingIgnoreCase(String name);

    /**
     * Find a supplier by email address.
     * 
     * @param email the email address
     * @return Optional containing the supplier if found
     */
    Optional<Supplier> findByEmail(String email);

    /**
     * Find suppliers by phone number.
     * 
     * @param phoneNumber the phone number to search for
     * @return list of suppliers with matching phone numbers
     */
    List<Supplier> findByPhoneNumber(String phoneNumber);

    /**
     * Check if a supplier with the given name exists.
     * 
     * @param name the name to check
     * @return true if a supplier with this name exists
     */
    boolean existsByName(String name);

    /**
     * Check if a supplier with the given email exists.
     * 
     * @param email the email to check
     * @return true if a supplier with this email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find all suppliers that have purchase records.
     * 
     * @return list of suppliers that have made sales
     */
    @Query("SELECT DISTINCT s FROM Supplier s JOIN s.purchases p")
    List<Supplier> findSuppliersWithPurchases();

    /**
     * Find suppliers ordered by name alphabetically.
     * 
     * @return list of suppliers sorted by name
     */
    List<Supplier> findAllByOrderByNameAsc();
}
