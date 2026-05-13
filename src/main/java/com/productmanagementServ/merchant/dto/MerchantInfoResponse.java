package com.productmanagementServ.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantInfoResponse {
    private String businessName;
    private String phone;
}
