package com.productmanagementServ.merchant.service;

import com.productmanagementServ.merchant.entity.Inventory;
import com.productmanagementServ.merchant.entity.Product;
import com.productmanagementServ.merchant.repository.InventoryRepository;
import com.productmanagementServ.merchant.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    
    public ProductService(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }
    
    public List<Product> getProductsByMerchant(Integer merchantId) {
        List<Product> products = productRepository.findByMerchantIdAndIsActiveTrue(merchantId);
        products.forEach(this::populateQuantity);
        return products;
    }
    
    public Product getProductById(Integer merchantId, Integer productId) {
        Product product = productRepository.findByIdAndMerchantId(productId, merchantId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        populateQuantity(product);
        return product;
    }

    private void populateQuantity(Product product) {
        inventoryRepository.findByProductId(product.getId())
                .ifPresent(inv -> product.setQuantity(inv.getQuantityOnHand()));
    }
    
    @Transactional
    public Product createProduct(Integer merchantId, Product product) {
        product.setMerchantId(merchantId);
        product.setIsActive(true);
        if (product.getSellingPrice() == null) {
            product.setSellingPrice(product.getMrp());
        }

        Product savedProduct = productRepository.save(product);
        
        // Create inventory entry
        Inventory inventory = new Inventory();
        inventory.setProductId(savedProduct.getId());
        inventory.setQuantityOnHand(0);
        inventory.setLowStockThreshold(5);
        inventoryRepository.save(inventory);
        
        return savedProduct;
    }
    
    @Transactional
    public Product updateProduct(Integer merchantId, Integer productId, Product productUpdate) {
        Product product = productRepository.findByIdAndMerchantId(productId, merchantId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (productUpdate.getName() != null) {
            product.setName(productUpdate.getName());
        }
        if (productUpdate.getDescription() != null) {
            product.setDescription(productUpdate.getDescription());
        }
        if (productUpdate.getSku() != null) {
            product.setSku(productUpdate.getSku());
        }
        if (productUpdate.getCategoryId() != null) {
            product.setCategoryId(productUpdate.getCategoryId());
        }
        if (productUpdate.getMrp() != null) {
            product.setMrp(productUpdate.getMrp());
        }
        if (productUpdate.getSellingPrice() != null) {
            product.setSellingPrice(productUpdate.getSellingPrice());
        }
        if (productUpdate.getImageUrl() != null) {
            product.setImageUrl(productUpdate.getImageUrl());
        }
        
        return productRepository.save(product);
    }
    
    @Transactional
    public void deleteProduct(Integer merchantId, Integer productId) {
        Product product = productRepository.findByIdAndMerchantId(productId, merchantId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setIsActive(false);
        productRepository.save(product);
    }
}