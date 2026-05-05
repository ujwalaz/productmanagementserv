package com.productmanagementServ.merchant.controller;

import com.productmanagementServ.merchant.entity.Inventory;
import com.productmanagementServ.merchant.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    
    @GetMapping
    public ResponseEntity<List<Inventory>> getInventory(Authentication authentication) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        List<Inventory> inventory = inventoryService.getInventoryByMerchant(merchantId);
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStock(Authentication authentication) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        List<Inventory> inventory = inventoryService.getLowStockByMerchant(merchantId);
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getInventoryByProduct(Authentication authentication,
                                                            @PathVariable Integer productId) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        Inventory inventory = inventoryService.getInventoryByProduct(merchantId, productId);
        return ResponseEntity.ok(inventory);
    }
    
    @PutMapping("/{productId}")
    public ResponseEntity<Inventory> updateInventory(Authentication authentication,
                                                      @PathVariable Integer productId,
                                                      @RequestBody Map<String, Integer> request) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        Integer quantity = request.get("quantity");
        Inventory updated = inventoryService.updateInventory(merchantId, productId, quantity);
        return ResponseEntity.ok(updated);
    }
}