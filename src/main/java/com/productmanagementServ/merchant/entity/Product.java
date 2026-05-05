package com.productmanagementServ.merchant.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "merchant_id", nullable = false)
    private Integer merchantId;
    
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;
    
    @Column(name = "sku", columnDefinition = "NVARCHAR(100)")
    private String sku;
    
    @Column(name = "mrp", precision = 10, scale = 2, nullable = false)
    private BigDecimal mrp;
    
    @Column(name = "selling_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal sellingPrice;
    
    @Column(name = "image_url", columnDefinition = "NVARCHAR(MAX)")
    private String imageUrl;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Transient
    private Integer quantity;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}