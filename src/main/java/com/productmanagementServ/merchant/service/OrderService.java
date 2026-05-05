package com.productmanagementServ.merchant.service;

import com.productmanagementServ.merchant.entity.Order;
import com.productmanagementServ.merchant.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public List<Order> getOrdersByMerchant(Integer merchantId) {
        return orderRepository.findByMerchantId(merchantId);
    }
    
    public List<Order> getOrdersByMerchantAndStatus(Integer merchantId, String status) {
        return orderRepository.findByMerchantIdAndStatus(merchantId, status);
    }
    
    public Order getOrderById(Integer merchantId, Integer orderId) {
        return orderRepository.findById(orderId)
                .filter(o -> o.getItems() != null && o.getItems().stream()
                        .anyMatch(i -> i.getMerchantId().equals(merchantId)))
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public Long countOrders(Integer merchantId) {
        return orderRepository.countByMerchantId(merchantId);
    }
    
    public Long countOrdersByStatus(Integer merchantId, String status) {
        return orderRepository.countByMerchantIdAndStatus(merchantId, status);
    }
}