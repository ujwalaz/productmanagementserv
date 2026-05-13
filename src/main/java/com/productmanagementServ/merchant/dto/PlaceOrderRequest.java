package com.productmanagementServ.merchant.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
    private String customerName;
    private AddressDto address;
    private List<OrderItemRequest> items;
}
