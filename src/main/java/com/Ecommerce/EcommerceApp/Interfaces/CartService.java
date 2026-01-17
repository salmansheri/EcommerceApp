package com.Ecommerce.EcommerceApp.Interfaces;

import java.util.List;

import com.Ecommerce.EcommerceApp.Dtos.CartDTO;

import jakarta.transaction.Transactional;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

}
