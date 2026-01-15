package com.Ecommerce.EcommerceApp.Interfaces;

import com.Ecommerce.EcommerceApp.Dtos.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity); 
    
}
