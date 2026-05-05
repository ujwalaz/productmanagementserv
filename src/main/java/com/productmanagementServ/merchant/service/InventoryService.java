package com.productmanagementServ.merchant.service;

import com.productmanagementServ.merchant.entity.Inventory;
import com.productmanagementServ.merchant.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    
    public List<Inventory> getInventoryByMerchant(Integer merchantId) {
        return inventoryRepository.findByMerchantId(merchantId);
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