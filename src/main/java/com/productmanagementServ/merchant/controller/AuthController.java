package com.productmanagementServ.merchant.controller;

import com.productmanagementServ.merchant.dto.CustomerAuthResponse;
import com.productmanagementServ.merchant.dto.LoginRequest;
import com.productmanagementServ.merchant.dto.LoginResponse;
import com.productmanagementServ.merchant.entity.Merchant;
import com.productmanagementServ.merchant.entity.MerchantAuth;
import com.productmanagementServ.merchant.security.JwtUtil;
import com.productmanagementServ.merchant.service.AuthService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        MerchantAuth auth = authService.validateCredentials(request.getPhoneNumber(), request.getPassword());
        String token = jwtUtil.generateMerchantToken(auth.getMerchantId());

        LoginResponse response = new LoginResponse(token, auth.getMerchantId());
        response.setScope(jwtUtil.getMerchantScope());

        try {
            Merchant merchant = authService.getMerchantDetails(auth.getMerchantId());
            response.setMerchantName(merchant.getBusinessName());
        } catch (Exception e) {
            // Merchant name is optional, continue without it
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerAuthResponse> customerAuth(@RequestBody java.util.Map<String, String> body) {
        String phone = body.get("phone");
        if (!StringUtils.hasText(phone)) {
            throw new RuntimeException("Phone number is required");
        }
        String token = jwtUtil.generateCustomerToken(phone);
        return ResponseEntity.ok(new CustomerAuthResponse(token, phone, jwtUtil.getCustomerScope()));
    }
}