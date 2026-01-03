package com.Ecommerce.EcommerceApp.Security.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private String jwtToken;
    private String username;
    private List<String> roles;

}
