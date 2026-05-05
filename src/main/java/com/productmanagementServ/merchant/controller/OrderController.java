package com.productmanagementServ.merchant.controller;

import com.productmanagementServ.merchant.entity.Order;
import com.productmanagementServ.merchant.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(Authentication authentication,
                                                  @RequestParam(required = false) String status) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());

        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderService.getOrdersByMerchantAndStatus(merchantId, status.toLowerCase());
        } else {
            orders = orderService.getOrdersByMerchant(merchantId);
        }
        
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(Authentication authentication,
                                           @PathVariable Integer orderId) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        Order order = orderService.getOrderById(merchantId, orderId);
        return ResponseEntity.ok(order);
    }
}