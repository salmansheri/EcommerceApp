package com.Ecommerce.EcommerceApp.Services;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.Ecommerce.EcommerceApp.Dtos.CartDTO;
import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Exceptions.ApiException;
import com.Ecommerce.EcommerceApp.Exceptions.ResourceNotFoundException;
import com.Ecommerce.EcommerceApp.Interfaces.CartService;
import com.Ecommerce.EcommerceApp.Lib.Utils.AuthUtils;
import com.Ecommerce.EcommerceApp.Mappers.CartMapper;
import com.Ecommerce.EcommerceApp.Mappers.ProductMapper;
import com.Ecommerce.EcommerceApp.Models.Cart;
import com.Ecommerce.EcommerceApp.Models.CartItem;
import com.Ecommerce.EcommerceApp.Models.Product;
import com.Ecommerce.EcommerceApp.Repositories.CartItemRepository;
import com.Ecommerce.EcommerceApp.Repositories.CartRepository;
import com.Ecommerce.EcommerceApp.Repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final AuthUtils authUtil;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductMapper productMapper;

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

        cartRepository.save(cart);

        CartDTO cartDTO = cartMapper.toDTO(cart);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDto> productDTOStream = cartItems.stream().map(item -> {
            ProductDto map = productMapper.toDto(item.getProduct());

            map.setQuantity(item.getQuantity());

            return map;
        });

        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;

    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();

        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);

        return newCart;
    }

    @Override
    // @Cacheable(value = "ecommerce::cartList")
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty()) {
            throw new ApiException("No Cart exists");
        }

        List<CartDTO> cartDTOs = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = cartMapper.toDTO(cart);
                    List<ProductDto> products = cart.getCartItems().stream()
                            .map(p -> {
                                ProductDto productDto = productMapper.toDto(p.getProduct());
                                productDto.setQuantity(p.getQuantity());
                                return productDto;

                            }).toList();

                    cartDTO.setProducts(products);

                    return cartDTO;

                }).toList();

        return cartDTOs;

    }

    @Override
    // @Cacheable(value = "ecommerce::cartByEmail", key="#emailId")
    public CartDTO getCart(String emailId) {
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();

        Cart userCart = cartRepository.findCartByEmailAndCartId(emailId, cartId);

        if (userCart == null) {
            throw new ResourceNotFoundException("Cart", "CardId", cartId);
        }

        CartDTO cartDTO = cartMapper.toDTO(userCart);

        userCart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));

        List<ProductDto> productDtos = userCart.getCartItems().stream()
                .map(p -> productMapper.toDto(p.getProduct())).toList();

        cartDTO.setProducts(productDtos);

        return cartDTO;

    }

    @Override
    @Transactional
   
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();

        Cart userCart = cartRepository.findCartByEmail(emailId);

        Cart cart = cartRepository.findById(userCart.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", userCart.getCartId()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        if (product.getQuantity() == 0) {
            throw new ApiException(product.getName() + "is not Available");
        }

        if (product.getQuantity() < quantity) {

            throw new ApiException("Please make an order of the " + product.getName()
                    + " less than or equal to the quality " + product.getQuantity());

        }

        if (product.getQuantity() == 0) {
            throw new ApiException(product.getName() + " is not available");

        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem == null) {
            throw new ApiException("Product " + product.getName() + " not available in the cart");
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0) {
            throw new ApiException("The resulting quantity cannot be negative");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(cart.getCartId(), productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());

            cartItem.setQuantity(cartItem.getQuantity() + quantity);

            cartItem.setDiscount(product.getDiscount());

            cart.setTotalPrice(userCart.getTotalPrice() + (cartItem.getProductPrice() * quantity));

            cartRepository.save(cart);

        }

        CartItem updateCartItem = cartItemRepository.save(cartItem);

        if (updateCartItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updateCartItem.getCartItemId());
        }

        CartDTO cartDTO = cartMapper.toDTO(cart);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDto> productStream = cartItems.stream().map(item -> {
            ProductDto productDto = productMapper.toDto(item.getProduct());

            productDto.setQuantity(item.getQuantity());

            return productDto;

        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;

    }

    @Override
    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "ProductId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product Successfully Delete from the cart";

    }

    @Override
    public void updateProductInCarts(Long cartId, Long id) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", cartId));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", id));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, id);

        if (cartItem == null) {
            throw new ApiException("Product " + product.getName() + " not available");
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);

    }

}
