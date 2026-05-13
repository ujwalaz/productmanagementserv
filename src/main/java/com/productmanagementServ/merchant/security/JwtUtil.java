package com.productmanagementServ.merchant.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final List<String> MERCHANT_SCOPE = Arrays.asList(
            "GET /api/products",
            "POST /api/products",
            "PUT /api/products/{id}",
            "DELETE /api/products/{id}",
            "GET /api/inventory",
            "GET /api/inventory/low-stock",
            "PUT /api/inventory/{productId}",
            "GET /api/orders",
            "GET /api/orders/{id}",
            "PATCH /api/orders/{id}/status",
            "PATCH /api/orders/{id}/cancel",
            "GET /api/merchant/info"
    );

    private static final List<String> CUSTOMER_SCOPE = Arrays.asList(
            "GET /api/products",
            "POST /api/orders",
            "GET /api/orders",
            "GET /api/orders/{id}",
            "GET /api/merchant/info"
    );

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateMerchantToken(Integer merchantId) {
        return Jwts.builder()
                .claim("role", "MERCHANT")
                .claim("merchantId", merchantId)
                .claim("scope", MERCHANT_SCOPE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateCustomerToken(String phone) {
        return Jwts.builder()
                .claim("role", "CUSTOMER")
                .claim("phone", phone)
                .claim("scope", CUSTOMER_SCOPE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public Integer getMerchantId(String token) {
        return getClaims(token).get("merchantId", Integer.class);
    }

    public String getPhone(String token) {
        return getClaims(token).get("phone", String.class);
    }

    public List<String> getMerchantScope() {
        return MERCHANT_SCOPE;
    }

    public List<String> getCustomerScope() {
        return CUSTOMER_SCOPE;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}