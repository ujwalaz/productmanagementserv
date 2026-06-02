package com.productmanagementServ.merchant.service;

import com.productmanagementServ.merchant.entity.Inventory;
import com.productmanagementServ.merchant.repository.InventoryRepository;
import com.productmanagementServ.merchant.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }
    
    public List<Inventory> getInventoryByMerchant(Integer merchantId) {
        List<Inventory> inventoryList = inventoryRepository.findByMerchantId(merchantId);
        Map<Integer, String> nameMap = productRepository.findByMerchantId(merchantId)
                .stream().collect(Collectors.toMap(p -> p.getId(), p -> p.getName()));
        inventoryList.forEach(inv -> inv.setProductName(nameMap.get(inv.getProductId())));
        return inventoryList;
    }
    
    public List<Inventory> getLowStockByMerchant(Integer merchantId) {
        return inventoryRepository.findLowStockByMerchantId(merchantId);
    }
    
    public Inventory getInventoryByProduct(Integer merchantId, Integer productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }
    
    @Transactional
    public Inventory updateInventory(Integer merchantId, Integer productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        
        inventory.setQuantityOnHand(quantity);
        return inventoryRepository.save(inventory);
    }
    
    public Long countLowStock(Integer merchantId) {
        return inventoryRepository.countLowStockByMerchantId(merchantId);
    }
    
    public Long countOutOfStock(Integer merchantId) {
        return inventoryRepository.countOutOfStockByMerchantId(merchantId);
    }
}