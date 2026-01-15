package com.Ecommerce.EcommerceApp.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cart;
    private ProductDto product;
    private Integer quantity;
    private Double discount;
    private Double productPrice;

}
