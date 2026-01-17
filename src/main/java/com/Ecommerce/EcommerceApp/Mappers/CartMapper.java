package com.Ecommerce.EcommerceApp.Mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.Ecommerce.EcommerceApp.Dtos.CartDTO;
import com.Ecommerce.EcommerceApp.Models.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "products", ignore = true)
    CartDTO toDTO(Cart cart); 

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Cart toEntity(CartDTO cartDTO);

    List<CartDTO> toDTOList(List<Cart> carts); 
    
}
