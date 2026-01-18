package com.Ecommerce.EcommerceApp.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.EcommerceApp.Dtos.ApiResponseDto;
import com.Ecommerce.EcommerceApp.Dtos.CartDTO;
import com.Ecommerce.EcommerceApp.Interfaces.CartService;
import com.Ecommerce.EcommerceApp.Lib.Utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    private final AuthUtils authUtils;

    @PostMapping("/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> postMethodName(@PathVariable Long productId, @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);

    }

    @GetMapping()
    public ResponseEntity<List<CartDTO>> getMethodName() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();

        return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.OK);
    }

    @GetMapping("/users/cart")
    public ResponseEntity<CartDTO> getCarById() {
        String emailId = authUtils.loggedInEmail();

        CartDTO cartDTO = cartService.getCart(emailId);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId, @PathVariable String operation) {
        Integer quantity = operation.equalsIgnoreCase("delete") ? -1 : 1;

        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, quantity);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);

    }

    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<ApiResponseDto> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {

        String message = cartService.deleteProductFromCart(cartId, productId);

        ApiResponseDto responseDto =  new ApiResponseDto(message, true); 
        
        return new ResponseEntity<ApiResponseDto>(responseDto, HttpStatus.OK); 

    }

}
