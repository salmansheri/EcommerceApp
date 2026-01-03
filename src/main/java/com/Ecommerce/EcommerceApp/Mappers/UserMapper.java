package com.Ecommerce.EcommerceApp.Mappers;

import java.util.List;

import org.mapstruct.Mapper;

import org.mapstruct.MappingTarget;

import com.Ecommerce.EcommerceApp.Dtos.UserDTO;
import com.Ecommerce.EcommerceApp.Models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    List<UserDTO> toDto(List<User> users);

    User toEntity(UserDTO productDto);

    void updateUserFromDto(UserDTO userDTO, @MappingTarget User user);

}
