package com.productmanagementServ.merchant.controller;

import com.productmanagementServ.merchant.dto.PlaceOrderRequest;
import com.productmanagementServ.merchant.dto.UpdateStatusRequest;
import com.productmanagementServ.merchant.entity.Order;
import com.productmanagementServ.merchant.service.CustomerService;
import com.productmanagementServ.merchant.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(Authentication authentication,
                                                  @RequestParam(required = false) String status) {
        if (hasRole(authentication, "ROLE_CUSTOMER")) {
            String phone = authentication.getPrincipal().toString();
            List<Order> orders = orderService.getOrdersByCustomerPhone(phone);
            return ResponseEntity.ok(orders);
        }

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
        if (hasRole(authentication, "ROLE_CUSTOMER")) {
            String phone = authentication.getPrincipal().toString();
            return ResponseEntity.ok(orderService.getOrderByIdForCustomer(phone, orderId));
        }
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        return ResponseEntity.ok(orderService.getOrderById(merchantId, orderId));
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(Authentication authentication,
                                             @RequestBody PlaceOrderRequest request) {
        if (!hasRole(authentication, "ROLE_CUSTOMER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String phone = authentication.getPrincipal().toString();
        Order order = customerService.placeOrder(phone, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(Authentication authentication,
                                                    @PathVariable Integer orderId,
                                                    @RequestBody UpdateStatusRequest request) {
        if (!hasRole(authentication, "ROLE_MERCHANT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        Order updated = orderService.updateStatus(merchantId, orderId, request.getStatus().toLowerCase());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(Authentication authentication,
                                              @PathVariable Integer orderId) {
        if (!hasRole(authentication, "ROLE_MERCHANT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        return ResponseEntity.ok(orderService.cancelOrder(merchantId, orderId));
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}