package com.productmanagementServ.merchant.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id", nullable = false, insertable = false, updatable = false)
    private Integer orderId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "merchant_id", nullable = false)
    private Integer merchantId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;
    
    @Column(name = "subtotal", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}