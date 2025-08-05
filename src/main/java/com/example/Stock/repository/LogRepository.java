package com.example.Stock.repository;

import com.example.Stock.entity.Log;
import com.example.Stock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Log entity operations.
 * Provides CRUD operations and custom queries for managing system activity logs
 * in the inventory management system.
 */
@Repository
public interface LogRepository extends JpaRepository<Log, UUID> {

    /**
     * Find logs by user.
     * 
     * @param user the user to filter by
     * @return list of logs for the specified user
     */
    List<Log> findByUser(User user);

    /**
     * Find logs by user ID.
     * 
     * @param userId the user ID to filter by
     * @return list of logs for the specified user
     */
    List<Log> findByUserUserId(UUID userId);

    /**
     * Find logs created after a specific date.
     * 
     * @param date the date to compare against
     * @return list of logs created after the specified date
     */
    List<Log> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find logs created between two dates.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of logs created within the specified date range
     */
    List<Log> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find logs by IP address.
     * 
     * @param ipAddress the IP address to filter by
     * @return list of logs from the specified IP address
     */
    List<Log> findByIpAddress(String ipAddress);

    /**
     * Find logs containing specific action text.
     * 
     * @param action the action text to search for
     * @return list of logs containing the specified action
     */
    List<Log> findByActionContainingIgnoreCase(String action);

    /**
     * Find recent logs (ordered by creation date, newest first).
     * 
     * @param limit the maximum number of logs to return
     * @return list of recent logs
     */
    @Query("SELECT l FROM Log l ORDER BY l.createdAt DESC")
    List<Log> findRecentLogs();

    /**
     * Find logs for a specific user within a date range.
     * 
     * @param user the user to filter by
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of logs for the user within the date range
     */
    List<Log> findByUserAndCreatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count logs by user.
     * 
     * @param user the user to count logs for
     * @return number of logs for the specified user
     */
    long countByUser(User user);

    /**
     * Find logs ordered by creation date (newest first).
     * 
     * @return list of logs sorted by creation date descending
     */
    List<Log> findAllByOrderByCreatedAtDesc();

    /**
     * Delete logs older than a specific date.
     * 
     * @param date the cutoff date
     * @return number of deleted logs
     */
    @Query("DELETE FROM Log l WHERE l.createdAt < :date")
    int deleteByCreatedAtBefore(@Param("date") LocalDateTime date);
}
