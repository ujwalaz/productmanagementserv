package com.productmanagementServ.merchant.service;

import com.productmanagementServ.merchant.dto.AddressDto;
import com.productmanagementServ.merchant.dto.OrderItemRequest;
import com.productmanagementServ.merchant.dto.PlaceOrderRequest;
import com.productmanagementServ.merchant.entity.*;
import com.productmanagementServ.merchant.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public CustomerService(CustomerRepository customerRepository,
                           CustomerAddressRepository customerAddressRepository,
                           ProductRepository productRepository,
                           InventoryRepository inventoryRepository,
                           OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository) {
        this.customerRepository = customerRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Customer upsertCustomer(String phone, String name) {
        return customerRepository.findByPhone(phone).map(existing -> {
            if (name != null && !name.isBlank()) {
                existing.setName(name);
                return customerRepository.save(existing);
            }
            return existing;
        }).orElseGet(() -> {
            Customer newCustomer = new Customer();
            newCustomer.setPhone(phone);
            newCustomer.setName(name != null ? name : phone);
            return customerRepository.save(newCustomer);
        });
    }

    @Transactional
    public Order placeOrder(String phone, PlaceOrderRequest request) {
        Customer customer = upsertCustomer(phone, request.getCustomerName());

        AddressDto addressDto = request.getAddress();
        CustomerAddress address = new CustomerAddress();
        address.setCustomerId(customer.getId());
        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry("India");
        address.setIsDefault(false);
        CustomerAddress savedAddress = customerAddressRepository.save(address);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemRequest> itemRequests = request.getItems();

        // Validate products and inventory before saving the order
        List<Product> products = new ArrayList<>();
        List<Inventory> inventories = new ArrayList<>();

        for (OrderItemRequest itemReq : itemRequests) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));
            if (!Boolean.TRUE.equals(product.getIsActive())) {
                throw new RuntimeException("Product is not available: " + product.getName());
            }
            Inventory inventory = inventoryRepository.findByProductId(product.getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + product.getId()));
            if (inventory.getQuantityOnHand() < itemReq.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            products.add(product);
            inventories.add(inventory);
            totalAmount = totalAmount.add(product.getSellingPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }

        Order order = new Order();
        order.setCustomerId(customer.getId());
        order.setShippingAddressId(savedAddress.getId());
        order.setStatus("pending");
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        for (int i = 0; i < itemRequests.size(); i++) {
            OrderItemRequest itemReq = itemRequests.get(i);
            Product product = products.get(i);
            Inventory inventory = inventories.get(i);

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProductId(product.getId());
            item.setMerchantId(product.getMerchantId());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(product.getSellingPrice());
            orderItemRepository.save(item);

            inventory.setQuantityOnHand(inventory.getQuantityOnHand() - itemReq.getQuantity());
            inventoryRepository.save(inventory);
        }

        return savedOrder;
    }
}
