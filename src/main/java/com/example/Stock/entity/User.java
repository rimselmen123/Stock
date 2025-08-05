package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing system users who can access and operate the stock management system.
 * Users have different roles that determine their access levels and permissions.
 * The system tracks user authentication, activity logs, and transaction history.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Unique identifier for the user.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "user_id")
    private UUID userId;

    /**
     * Unique username for system login.
     * Required field with maximum length of 50 characters.
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Encrypted password for user authentication.
     * Required field with maximum length of 255 characters to accommodate hashing.
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * User's role in the system determining access permissions.
     * Defaults to CASHIER role if not specified.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private UserRole role = UserRole.CASHIER;

    /**
     * Timestamp when the user account was created.
     * Automatically set to current timestamp on creation.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp of the user's last successful login.
     * Updated each time the user logs into the system.
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /**
     * Activity logs associated with this user.
     * Tracks user actions, logins, and system interactions.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Log> logs;

    /**
     * Sales transactions processed by this user.
     * One user can process multiple sales.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> sales;

    /**
     * Transfer transactions initiated by this user.
     * One user can initiate multiple transfers.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transfer> transfers;

    /**
     * Stock movements recorded by this user.
     * One user can record multiple stock movements.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockMovement> stockMovements;

    /**
     * Inventory sessions started by this user.
     * One user can start multiple inventory counting sessions.
     */
    @OneToMany(mappedBy = "startedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventorySession> inventorySessions;

    /**
     * Automatically set creation timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
