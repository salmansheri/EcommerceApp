package com.Ecommerce.EcommerceApp.Controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.EcommerceApp.Dtos.MessageResponseDTO;
import com.Ecommerce.EcommerceApp.Dtos.SignUpRequestDTO;
import com.Ecommerce.EcommerceApp.Interfaces.AuthService;
import com.Ecommerce.EcommerceApp.Security.DTOs.LoginRequestDTO;
import com.Ecommerce.EcommerceApp.Security.DTOs.LoginResponseDTO;
import com.Ecommerce.EcommerceApp.Security.services.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for Authention Provides Endpoint for Signup, Sign in
 *
 * The Business logic is Delegation to {@link AuthService}
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/sign-in")
	public ResponseEntity<LoginResponseDTO> signIn(@RequestBody LoginRequestDTO loginRequestDTO) {
		LoginResponseDTO responseDTO = authService.signIn(loginRequestDTO);

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseDTO.getJwtCookie().toString())
				.body(responseDTO);

	}

	@PostMapping("/sign-up")
	public ResponseEntity<MessageResponseDTO> signUp(@Valid @RequestBody SignUpRequestDTO requestDTO) {
		MessageResponseDTO messageDTO = authService.signUp(requestDTO);

		return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
	}

	@GetMapping("/currentUser")
	public ResponseEntity<LoginResponseDTO> getCurrentUserName(Authentication authentication) {
		LoginResponseDTO responseDTO = authService.getCurrentUsername(authentication);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);

	}

	@GetMapping("/user")
	public ResponseEntity<UserDetailsImpl> getCurrentUserDetails(Authentication authentication) {

		return new ResponseEntity<>(authService.getCurrentUserDetails(authentication), HttpStatus.OK);

	}

	@PostMapping("/signOut")
	public ResponseEntity<LoginResponseDTO> signOut() {
		LoginResponseDTO responseDTO = authService.signOut();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseDTO.getJwtCookie().toString())
				.body(responseDTO);

	}

}
