package com.Ecommerce.EcommerceApp.Services;

import org.springframework.stereotype.Service;

import com.Ecommerce.EcommerceApp.Dtos.CartDTO;
import com.Ecommerce.EcommerceApp.Exceptions.ApiException;
import com.Ecommerce.EcommerceApp.Exceptions.ResourceNotFoundException;
import com.Ecommerce.EcommerceApp.Interfaces.CartService;
import com.Ecommerce.EcommerceApp.Lib.Utils.AuthUtils;
import com.Ecommerce.EcommerceApp.Mappers.CartMapper;
import com.Ecommerce.EcommerceApp.Models.Cart;
import com.Ecommerce.EcommerceApp.Models.CartItem;
import com.Ecommerce.EcommerceApp.Models.Product;
import com.Ecommerce.EcommerceApp.Repositories.CartItemRepository;
import com.Ecommerce.EcommerceApp.Repositories.CartRepository;
import com.Ecommerce.EcommerceApp.Repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private  AuthUtils authUtil;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new ApiException("Product " + product.getName() + "already exist in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new ApiException(product.getName() + "is not available!");
        }

        if (product.getQuantity() < quantity) {
            throw new ApiException("Please make an order of the " + product.getName()
                    + "less than or equal to quantity " + product.getQuantity() + ".");
        }

        

        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        Cart updatedCart = cartRepository.save(cart);

        return cartMapper.toDTO(updatedCart);

    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();

        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser);
        Cart newCart = cartRepository.save(cart);

        return newCart;
    }

}
