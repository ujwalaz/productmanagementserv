package com.productmanagementServ.merchant.controller;

import com.productmanagementServ.merchant.entity.Product;
import com.productmanagementServ.merchant.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(Authentication authentication) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        List<Product> products = productService.getProductsByMerchant(merchantId);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(Authentication authentication,
                                               @PathVariable Integer productId) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        Product product = productService.getProductById(merchantId, productId);
        return ResponseEntity.ok(product);
    }
    
    @PostMapping
    public ResponseEntity<Product> createProduct(Authentication authentication,
                                                  @RequestBody Product product) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        Product created = productService.createProduct(merchantId, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(Authentication authentication,
                                                  @PathVariable Integer productId,
                                                  @RequestBody Product product) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        Product updated = productService.updateProduct(merchantId, productId, product);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(Authentication authentication,
                                               @PathVariable Integer productId) {
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        productService.deleteProduct(merchantId, productId);
        return ResponseEntity.noContent().build();
    }
}