package com.productmanagementServ.merchant.controller;

import com.productmanagementServ.merchant.dto.LoginRequest;
import com.productmanagementServ.merchant.dto.LoginResponse;
import com.productmanagementServ.merchant.entity.Merchant;
import com.productmanagementServ.merchant.entity.MerchantAuth;
import com.productmanagementServ.merchant.service.AuthService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        MerchantAuth auth = authService.validateCredentials(request.getPhoneNumber(), request.getPassword());
        String token = authService.generateToken(auth.getMerchantId());
        
        LoginResponse response = new LoginResponse(token, auth.getMerchantId());
        
        try {
            Merchant merchant = authService.getMerchantDetails(auth.getMerchantId());
            response.setMerchantName(merchant.getBusinessName());
        } catch (Exception e) {
            // Merchant name is optional, continue without it
        }
        
        return ResponseEntity.ok(response);
    }
}