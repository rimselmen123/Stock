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
 * Entity representing inventory counting sessions for physical stock verification.
 * Inventory sessions are used to conduct periodic stock counts to verify
 * that physical inventory matches the recorded quantities in the system.
 */
@Entity
@Table(name = "inventory_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventorySession {

    /**
     * Unique identifier for the inventory session.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "session_id")
    private UUID sessionId;

    /**
     * Timestamp when the inventory session was started.
     * Automatically set to current timestamp on creation.
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * Timestamp when the inventory session was completed.
     * Set when the session status is changed to CLOSED.
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * Current status of the inventory session.
     * Defaults to OPEN when a new session is created.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private InventoryStatus status = InventoryStatus.OPEN;

    /**
     * User who started this inventory session.
     * Required relationship - each session must be initiated by a user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "started_by", nullable = false)
    private User startedBy;

    /**
     * Location where this inventory session is being conducted.
     * Required relationship - each session must be associated with a location.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    /**
     * Individual inventory line items counted during this session.
     * One session can have multiple inventory lines for different products.
     */
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventoryLine> inventoryLines;

    /**
     * Automatically set start timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        startTime = LocalDateTime.now();
    }
}
