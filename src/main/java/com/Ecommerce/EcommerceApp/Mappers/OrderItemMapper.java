package com.Ecommerce.EcommerceApp.Mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.Ecommerce.EcommerceApp.Dtos.OrderItemDTO;
import com.Ecommerce.EcommerceApp.Models.OrderItem;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderItemMapper {

    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    List<OrderItemDTO> toDTOList(List<OrderItem> orderItems);

    @Mapping(target = "order", ignore = true)

    void updateOrderItemFromDto(OrderItemDTO orderItemDTO, @MappingTarget OrderItem existingOrderItem);

}
