package com.productmanagementServ.merchant.service;

import com.productmanagementServ.merchant.entity.Merchant;
import com.productmanagementServ.merchant.entity.MerchantAuth;
import com.productmanagementServ.merchant.repository.MerchantAuthRepository;
import com.productmanagementServ.merchant.repository.MerchantRepository;
import com.productmanagementServ.merchant.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private final MerchantAuthRepository merchantAuthRepository;
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthService(MerchantAuthRepository merchantAuthRepository,
                       MerchantRepository merchantRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.merchantAuthRepository = merchantAuthRepository;
        this.merchantRepository = merchantRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    public MerchantAuth validateCredentials(String phone, String password) {
        // Find merchant by phone, then get auth by merchantId
        Merchant merchant = merchantRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        MerchantAuth auth = merchantAuthRepository.findByMerchantId(merchant.getId())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        if (!passwordEncoder.matches(password, auth.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        return auth;
    }
    
    public String generateToken(Integer merchantId) {
        return jwtUtil.generate(merchantId);
    }
    
    public Merchant getMerchantDetails(Integer merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Merchant not found"));
    }
    
    public MerchantAuth createMerchantAuth(Integer merchantId, String password) {
        MerchantAuth auth = new MerchantAuth();
        auth.setMerchantId(merchantId);
        auth.setPasswordHash(passwordEncoder.encode(password));
        return merchantAuthRepository.save(auth);
    }
}