package com.productmanagementServ.merchant.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotBlank(message = "Password is required")
    private String password;
}