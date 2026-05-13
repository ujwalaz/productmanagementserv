package com.productmanagementServ.merchant.controller;

import com.productmanagementServ.merchant.dto.MerchantInfoResponse;
import com.productmanagementServ.merchant.entity.Merchant;
import com.productmanagementServ.merchant.repository.MerchantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/merchant")
public class MerchantInfoController {

    private final MerchantRepository merchantRepository;

    public MerchantInfoController(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @GetMapping("/info")
    public ResponseEntity<MerchantInfoResponse> getMerchantInfo() {
        List<Merchant> merchants = merchantRepository.findAll();
        if (merchants.isEmpty()) {
            throw new RuntimeException("Merchant not found");
        }
        Merchant merchant = merchants.get(0);
        return ResponseEntity.ok(new MerchantInfoResponse(merchant.getBusinessName(), merchant.getPhone()));
    }
}
