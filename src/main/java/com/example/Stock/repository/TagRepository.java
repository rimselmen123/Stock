package com.example.Stock.repository;

import com.example.Stock.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Tag entity operations.
 * Provides CRUD operations and custom queries for managing product tags
 * in the inventory management system.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    /**
     * Find a tag by its name.
     * 
     * @param name the name of the tag
     * @return Optional containing the tag if found
     */
    Optional<Tag> findByName(String name);

    /**
     * Find tags whose names contain the specified text (case-insensitive).
     * 
     * @param name the text to search for in tag names
     * @return list of tags matching the search criteria
     */
    List<Tag> findByNameContainingIgnoreCase(String name);

    /**
     * Check if a tag with the given name exists.
     * 
     * @param name the name to check
     * @return true if a tag with this name exists
     */
    boolean existsByName(String name);

    /**
     * Find all tags that are associated with products.
     * 
     * @return list of tags that are used by at least one product
     */
    @Query("SELECT DISTINCT t FROM Tag t JOIN t.products p")
    List<Tag> findTagsWithProducts();

    /**
     * Find tags ordered by name alphabetically.
     * 
     * @return list of tags sorted by name
     */
    List<Tag> findAllByOrderByNameAsc();

    /**
     * Find tags that are not associated with any products.
     * 
     * @return list of unused tags
     */
    @Query("SELECT t FROM Tag t WHERE t.products IS EMPTY")
    List<Tag> findUnusedTags();
}
