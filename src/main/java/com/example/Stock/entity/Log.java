package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing system activity logs for auditing and monitoring purposes.
 * Logs track user actions, system events, login attempts, and other important activities
 * for security, debugging, and compliance purposes.
 */
@Entity
@Table(name = "logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    /**
     * Unique identifier for the log entry.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "log_id")
    private UUID logId;

    /**
     * Description of the action or event that was logged.
     * Can include user actions, system events, errors, or other activities.
     */
    @Column(name = "action", columnDefinition = "TEXT")
    private String action;

    /**
     * IP address from which the action was performed.
     * Useful for security monitoring and tracking user locations.
     * Maximum length of 45 characters to accommodate IPv6 addresses.
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * User agent string from the client browser or application.
     * Helps identify the client software and platform used.
     */
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    /**
     * Timestamp when the log entry was created.
     * Automatically set to current timestamp on creation.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * User who performed the action being logged.
     * Can be null for system-generated logs or anonymous actions.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Automatically set creation timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
