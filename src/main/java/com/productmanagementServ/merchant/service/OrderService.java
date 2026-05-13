package com.productmanagementServ.merchant.service;

import com.productmanagementServ.merchant.entity.Customer;
import com.productmanagementServ.merchant.entity.Order;
import com.productmanagementServ.merchant.repository.CustomerRepository;
import com.productmanagementServ.merchant.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private static final Map<String, List<String>> VALID_TRANSITIONS = Map.of(
            "pending", Arrays.asList("confirmed", "cancelled"),
            "confirmed", Arrays.asList("shipped", "cancelled"),
            "shipped", Arrays.asList("delivered", "cancelled"),
            "delivered", List.of(),
            "cancelled", List.of()
    );

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public List<Order> getOrdersByMerchant(Integer merchantId) {
        return orderRepository.findByMerchantId(merchantId);
    }

    public List<Order> getOrdersByMerchantAndStatus(Integer merchantId, String status) {
        return orderRepository.findByMerchantIdAndStatus(merchantId, status);
    }

    public List<Order> getOrdersByCustomerPhone(String phone) {
        return orderRepository.findByCustomerPhone(phone);
    }

    public Order getOrderById(Integer merchantId, Integer orderId) {
        return orderRepository.findById(orderId)
                .filter(o -> o.getItems() != null && o.getItems().stream()
                        .anyMatch(i -> i.getMerchantId().equals(merchantId)))
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order getOrderByIdForCustomer(String phone, Integer orderId) {
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return orderRepository.findById(orderId)
                .filter(o -> o.getCustomerId().equals(customer.getId()))
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order updateStatus(Integer merchantId, Integer orderId, String newStatus) {
        Order order = getOrderById(merchantId, orderId);
        List<String> allowed = VALID_TRANSITIONS.getOrDefault(order.getStatus(), List.of());
        if (!allowed.contains(newStatus)) {
            throw new RuntimeException("Invalid status transition from '" + order.getStatus() + "' to '" + newStatus + "'");
        }
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Integer merchantId, Integer orderId) {
        Order order = getOrderById(merchantId, orderId);
        if ("delivered".equals(order.getStatus()) || "cancelled".equals(order.getStatus())) {
            throw new RuntimeException("Cannot cancel an order with status: " + order.getStatus());
        }
        order.setStatus("cancelled");
        return orderRepository.save(order);
    }

    public Long countOrders(Integer merchantId) {
        return orderRepository.countByMerchantId(merchantId);
    }

    public Long countOrdersByStatus(Integer merchantId, String status) {
        return orderRepository.countByMerchantIdAndStatus(merchantId, status);
    }
}