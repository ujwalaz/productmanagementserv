package com.productmanagementServ.merchant.controller;

import com.productmanagementServ.merchant.dto.ScanResultResponse;
import com.productmanagementServ.merchant.service.AzureVisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/merchant")
public class ScanController {

    private final AzureVisionService azureVisionService;

    public ScanController(AzureVisionService azureVisionService) {
        this.azureVisionService = azureVisionService;
    }

    @PostMapping("/scan")
    public ResponseEntity<ScanResultResponse> scan(
            @RequestParam("image") MultipartFile image,
            Authentication authentication) {
        if (!hasRole(authentication, "ROLE_MERCHANT")) {
            return ResponseEntity.status(403).build();
        }
        try {
            byte[] bytes = image.getBytes();
            ScanResultResponse result = azureVisionService.extractText(bytes);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException("Image scan failed: " + e.getMessage());
        }
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }
}
