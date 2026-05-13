package com.productmanagementServ.merchant.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customer_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "label", columnDefinition = "NVARCHAR(50)")
    private String label;

    @Column(name = "street", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String street;

    @Column(name = "city", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String city;

    @Column(name = "state", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String state;

    @Column(name = "postal_code", nullable = false, columnDefinition = "NVARCHAR(20)")
    private String postalCode;

    @Column(name = "country", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String country = "India";

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        if (country == null) country = "India";
        if (isDefault == null) isDefault = false;
    }
}
