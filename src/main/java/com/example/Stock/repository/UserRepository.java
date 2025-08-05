package com.example.Stock.repository;

import com.example.Stock.entity.User;
import com.example.Stock.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity operations.
 * Provides CRUD operations and custom queries for managing system users
 * in the inventory management system.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find a user by username.
     * 
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find users by role.
     * 
     * @param role the user role to filter by
     * @return list of users with the specified role
     */
    List<User> findByRole(UserRole role);

    /**
     * Check if a user with the given username exists.
     * 
     * @param username the username to check
     * @return true if a user with this username exists
     */
    boolean existsByUsername(String username);

    /**
     * Find users who have logged in after a specific date.
     * 
     * @param date the date to compare against
     * @return list of users who logged in after the specified date
     */
    List<User> findByLastLoginAfter(LocalDateTime date);

    /**
     * Find users created after a specific date.
     * 
     * @param date the date to compare against
     * @return list of users created after the specified date
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find users who have never logged in.
     * 
     * @return list of users with null last_login
     */
    List<User> findByLastLoginIsNull();

    /**
     * Find active users (those who have logged in within a specified period).
     * 
     * @param since the date from which to consider users active
     * @return list of active users
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin >= :since")
    List<User> findActiveUsers(@Param("since") LocalDateTime since);

    /**
     * Count users by role.
     * 
     * @param role the role to count
     * @return number of users with the specified role
     */
    long countByRole(UserRole role);

    /**
     * Find users ordered by creation date (newest first).
     * 
     * @return list of users sorted by creation date descending
     */
    List<User> findAllByOrderByCreatedAtDesc();

    /**
     * Find users ordered by last login date (most recent first).
     * 
     * @return list of users sorted by last login date descending
     */
    List<User> findAllByOrderByLastLoginDesc();
}
