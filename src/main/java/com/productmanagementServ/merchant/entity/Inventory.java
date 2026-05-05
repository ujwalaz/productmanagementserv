package com.productmanagementServ.merchant.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Integer productId;

    @Column(name = "quantity_on_hand", nullable = false)
    private Integer quantityOnHand = 0;
    
    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 5;

    @Column(name = "out_of_stock_alert_at", columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime outOfStockAlertAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        updatedAt = OffsetDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}