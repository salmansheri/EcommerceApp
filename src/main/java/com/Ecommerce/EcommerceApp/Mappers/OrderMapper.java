package com.Ecommerce.EcommerceApp.Mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.Ecommerce.EcommerceApp.Dtos.OrderDTO;
import com.Ecommerce.EcommerceApp.Models.Order;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, ProductMapper.class, PaymentMapper.class})
public interface OrderMapper {

       @Mapping(target = "addressId", ignore = true )
    OrderDTO toDto(Order order);

       @Mapping(target = "address", ignore = true )
    Order toEntity(OrderDTO orderDTO);

    List<OrderDTO> toDTOList(List<Order> orders);

       @Mapping(target = "address", ignore = true )
    void updateOrderFromDto(OrderDTO orderDTO, @MappingTarget Order existingOrder);

}
