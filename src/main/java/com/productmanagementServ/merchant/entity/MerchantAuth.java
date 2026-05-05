package com.productmanagementServ.merchant.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "merchant_auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantAuth {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "merchant_id", nullable = false, unique = true)
    private Integer merchantId;

    @Column(name = "password_hash", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String passwordHash;
    
    @Column(name = "last_login_at", columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime lastLoginAt;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}