package com.productmanagementServ.merchant.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer merchantId;
    private String merchantName;
    private List<String> scope;

    public LoginResponse(String token, Integer merchantId) {
        this.token = token;
        this.merchantId = merchantId;
    }
}