package com.example.Stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing suppliers who provide products to the business.
 * Suppliers are external entities from whom products are purchased
 * and are essential for tracking purchase history and vendor relationships.
 */
@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    /**
     * Unique identifier for the supplier.
     * Generated automatically using UUID.
     */
    @Id
    @UuidGenerator
    @Column(name = "supplier_id")
    private UUID supplierId;

    /**
     * Name of the supplier company or individual.
     * Required field with maximum length of 100 characters.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * General contact information for the supplier.
     * Optional field for storing additional contact details.
     */
    @Column(name = "contact_info", columnDefinition = "TEXT")
    private String contactInfo;

    /**
     * Primary phone number for contacting the supplier.
     * Optional field with maximum length of 20 characters.
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /**
     * Email address for electronic communication with the supplier.
     * Optional field with maximum length of 50 characters.
     */
    @Column(name = "email", length = 50)
    private String email;

    /**
     * Purchase records from this supplier.
     * One supplier can have multiple purchase transactions.
     */
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases;
}
