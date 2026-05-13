package com.productmanagementServ.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAuthResponse {
    private String token;
    private String phone;
    private List<String> scope;
}
