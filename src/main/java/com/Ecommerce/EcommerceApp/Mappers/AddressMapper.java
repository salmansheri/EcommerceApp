package com.Ecommerce.EcommerceApp.Mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.Ecommerce.EcommerceApp.Dtos.AddressDTO;
import com.Ecommerce.EcommerceApp.Models.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDTO toDto(Address address);

    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressDTO addressDTO);

    List<AddressDTO> toDTOList(List<Address> addresses);

    @Mapping(target = "addressId", ignore = true)
    @Mapping(target = "user", ignore = true)

    void updateAddressFromDto(AddressDTO addressDTO, @MappingTarget Address existingAddress);

}
