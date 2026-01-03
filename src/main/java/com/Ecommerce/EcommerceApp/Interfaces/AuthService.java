package com.Ecommerce.EcommerceApp.Interfaces;

import com.Ecommerce.EcommerceApp.Dtos.MessageResponseDTO;
import com.Ecommerce.EcommerceApp.Dtos.SignUpRequestDTO;
import com.Ecommerce.EcommerceApp.Security.DTOs.LoginRequestDTO;
import com.Ecommerce.EcommerceApp.Security.DTOs.LoginResponseDTO;

public interface AuthService {

	LoginResponseDTO signIn(LoginRequestDTO requestDTO);

	MessageResponseDTO signUp(SignUpRequestDTO requestDTO);

}
