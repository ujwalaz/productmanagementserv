package com.productmanagementServ.merchant.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String street;
    private String city;
    private String state;
    private String postalCode;
}
