package com.Ecommerce.EcommerceApp.Dtos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.Ecommerce.EcommerceApp.Models.Address;
import com.Ecommerce.EcommerceApp.Models.Product;
import com.Ecommerce.EcommerceApp.Models.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long userId;

	private String username;

	private String email;

	private String password;

	private Set<RoleDto> roles = new HashSet<>();

	private List<ProductDto> products;

	private List<AddressDTO> addresses = new ArrayList<>();

}
