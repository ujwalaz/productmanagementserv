package com.productmanagementServ.merchant.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "merchants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "business_name", nullable = false, columnDefinition = "NVARCHAR(150)")
    private String businessName;

    @Column(name = "contact_name", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String contactName;

    @Column(name = "phone", nullable = false, unique = true, columnDefinition = "NVARCHAR(20)")
    private String phone;

    @Column(name = "email", unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;
    
    @Column(name = "address", columnDefinition = "NVARCHAR(MAX)")
    private String address;
    
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}