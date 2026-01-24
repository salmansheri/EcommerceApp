package com.Ecommerce.EcommerceApp.Mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.Ecommerce.EcommerceApp.Dtos.PaymentDTO;
import com.Ecommerce.EcommerceApp.Models.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDTO toDto(Payment payment);

       @Mapping(target = "order", ignore = true )
    Payment toEntity(PaymentDTO paymentDTO);

    List<PaymentDTO> toDTOList(List<Payment> payments);

  
   @Mapping(target = "order", ignore = true )
    void updateAddressFromDto(PaymentDTO paymentDTO, @MappingTarget Payment existingPayment);

}
