package com.Ecommerce.EcommerceApp.Security.DTOs;

import java.util.List;

import org.springframework.http.ResponseCookie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

	

    private Long id;

	private ResponseCookie   jwtCookie;

	private String username;

	private List<String> roles;

}
