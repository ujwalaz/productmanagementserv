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
        if (hasRole(authentication, "ROLE_CUSTOMER")) {
            return ResponseEntity.ok(productService.getAllActiveProducts());
        }
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        return ResponseEntity.ok(productService.getProductsByMerchant(merchantId));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(Authentication authentication,
                                               @PathVariable Integer productId) {
        if (hasRole(authentication, "ROLE_CUSTOMER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        return ResponseEntity.ok(productService.getProductById(merchantId, productId));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(Authentication authentication,
                                                  @RequestBody Product product) {
        if (!hasRole(authentication, "ROLE_MERCHANT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(merchantId, product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(Authentication authentication,
                                                  @PathVariable Integer productId,
                                                  @RequestBody Product product) {
        if (!hasRole(authentication, "ROLE_MERCHANT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        return ResponseEntity.ok(productService.updateProduct(merchantId, productId, product));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(Authentication authentication,
                                               @PathVariable Integer productId) {
        if (!hasRole(authentication, "ROLE_MERCHANT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Integer merchantId = Integer.parseInt(authentication.getPrincipal().toString());
        productService.deleteProduct(merchantId, productId);
        return ResponseEntity.noContent().build();
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}