package com.Ecommerce.EcommerceApp.Interfaces;

import org.springframework.security.core.Authentication;

import com.Ecommerce.EcommerceApp.Dtos.MessageResponseDTO;
import com.Ecommerce.EcommerceApp.Dtos.SignUpRequestDTO;
import com.Ecommerce.EcommerceApp.Security.DTOs.LoginRequestDTO;
import com.Ecommerce.EcommerceApp.Security.DTOs.LoginResponseDTO;
import com.Ecommerce.EcommerceApp.Security.services.UserDetailsImpl;

public interface AuthService {

	LoginResponseDTO signIn(LoginRequestDTO requestDTO);

	MessageResponseDTO signUp(SignUpRequestDTO requestDTO);
	LoginResponseDTO getCurrentUsername(Authentication authentication); 

	UserDetailsImpl getCurrentUserDetails(Authentication authentication); 

	LoginResponseDTO signOut(); 

}
