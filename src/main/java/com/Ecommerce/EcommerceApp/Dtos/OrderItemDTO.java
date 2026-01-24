package com.Ecommerce.EcommerceApp.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long orderItemId;

    private ProductDto product;

    private Integer quantity;
    private Double discount;
    private Double orderedProductPrice;

}
