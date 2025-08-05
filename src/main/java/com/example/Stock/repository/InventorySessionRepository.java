package com.example.Stock.repository;

import com.example.Stock.entity.InventorySession;
import com.example.Stock.entity.InventoryStatus;
import com.example.Stock.entity.Location;
import com.example.Stock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for InventorySession entity operations.
 * Provides CRUD operations and custom queries for managing inventory counting sessions
 * in the inventory management system.
 */
@Repository
public interface InventorySessionRepository extends JpaRepository<InventorySession, UUID> {

    /**
     * Find inventory sessions by status.
     * 
     * @param status the status to filter by
     * @return list of inventory sessions with the specified status
     */
    List<InventorySession> findByStatus(InventoryStatus status);

    /**
     * Find inventory sessions by location.
     * 
     * @param location the location to search for
     * @return list of inventory sessions for the specified location
     */
    List<InventorySession> findByLocation(Location location);

    /**
     * Find inventory sessions started by a specific user.
     * 
     * @param startedBy the user who started the sessions
     * @return list of inventory sessions started by the specified user
     */
    List<InventorySession> findByStartedBy(User startedBy);

    /**
     * Find inventory sessions started after a specific date.
     * 
     * @param date the date to compare against
     * @return list of inventory sessions started after the specified date
     */
    List<InventorySession> findByStartTimeAfter(LocalDateTime date);

    /**
     * Find inventory sessions started between two dates.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of inventory sessions started within the specified date range
     */
    List<InventorySession> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find inventory sessions ended between two dates.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of inventory sessions ended within the specified date range
     */
    List<InventorySession> findByEndTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find open inventory sessions for a specific location.
     * 
     * @param location the location
     * @return list of open inventory sessions for the location
     */
    List<InventorySession> findByLocationAndStatus(Location location, InventoryStatus status);

    /**
     * Find the most recent inventory session for a location.
     * 
     * @param location the location
     * @return Optional containing the most recent session if found
     */
    @Query("SELECT i FROM InventorySession i WHERE i.location = :location " +
           "ORDER BY i.startTime DESC")
    Optional<InventorySession> findMostRecentSessionForLocation(@Param("location") Location location);

    /**
     * Find currently open inventory sessions.
     * 
     * @return list of open inventory sessions
     */
    @Query("SELECT i FROM InventorySession i WHERE i.status = 'OPEN'")
    List<InventorySession> findOpenSessions();

    /**
     * Find inventory sessions that have been open for longer than specified hours.
     * 
     * @param hours the number of hours
     * @return list of sessions open longer than the specified time
     */
    @Query("SELECT i FROM InventorySession i WHERE i.status = 'OPEN' " +
           "AND i.startTime < :cutoffTime")
    List<InventorySession> findLongRunningSessions(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Check if there's an open inventory session for a location.
     * 
     * @param location the location to check
     * @return true if there's an open session for the location
     */
    @Query("SELECT COUNT(i) > 0 FROM InventorySession i WHERE i.location = :location AND i.status = 'OPEN'")
    boolean hasOpenSessionForLocation(@Param("location") Location location);

    /**
     * Find recent inventory sessions (ordered by start time, newest first).
     * 
     * @return list of recent inventory sessions
     */
    List<InventorySession> findAllByOrderByStartTimeDesc();

    /**
     * Find inventory sessions by user and date range.
     * 
     * @param user the user
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of inventory sessions by the user within the date range
     */
    List<InventorySession> findByStartedByAndStartTimeBetween(User user, 
                                                             LocalDateTime startDate, 
                                                             LocalDateTime endDate);

    /**
     * Count inventory sessions by status.
     * 
     * @param status the status to count
     * @return number of inventory sessions with the specified status
     */
    long countByStatus(InventoryStatus status);

    /**
     * Count inventory sessions by user.
     * 
     * @param user the user to count sessions for
     * @return number of inventory sessions started by the specified user
     */
    long countByStartedBy(User user);

    /**
     * Find completed inventory sessions with discrepancies.
     * 
     * @return list of closed sessions that have inventory lines with differences
     */
    @Query("SELECT DISTINCT i FROM InventorySession i JOIN i.inventoryLines il " +
           "WHERE i.status = 'CLOSED' AND (il.countedQuantity != il.expectedQuantity)")
    List<InventorySession> findSessionsWithDiscrepancies();
}
