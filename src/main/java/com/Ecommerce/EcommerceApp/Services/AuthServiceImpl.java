package com.Ecommerce.EcommerceApp.Services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Ecommerce.EcommerceApp.Dtos.MessageResponseDTO;
import com.Ecommerce.EcommerceApp.Dtos.SignUpRequestDTO;
import com.Ecommerce.EcommerceApp.Exceptions.ApiException;
import com.Ecommerce.EcommerceApp.Exceptions.UnauthorizedException;
import com.Ecommerce.EcommerceApp.Interfaces.AuthService;
import com.Ecommerce.EcommerceApp.Models.AppRole;
import com.Ecommerce.EcommerceApp.Models.Role;
import com.Ecommerce.EcommerceApp.Models.User;
import com.Ecommerce.EcommerceApp.Repositories.RoleRepository;
import com.Ecommerce.EcommerceApp.Repositories.UserRepository;
import com.Ecommerce.EcommerceApp.Security.DTOs.LoginRequestDTO;
import com.Ecommerce.EcommerceApp.Security.DTOs.LoginResponseDTO;
import com.Ecommerce.EcommerceApp.Security.Jwt.JwtUtils;
import com.Ecommerce.EcommerceApp.Security.services.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final JwtUtils jwtUtils;

	private final AuthenticationManager authenticationManager;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final RoleRepository roleRepository;

	@Override
	public LoginResponseDTO signIn(LoginRequestDTO requestDTO) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

			List<String> roles = userDetails.getAuthorities()
					.stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());

			LoginResponseDTO responseDTO = new LoginResponseDTO(userDetails.getId(), jwtCookie,
					userDetails.getUsername(), roles);

			return responseDTO;

		} catch (AuthenticationException exception) {

			// throw new AuthenticationException(map.toString());
			throw new BadCredentialsException("Invalid username and password");

		}

	}

	@Override
	public MessageResponseDTO signUp(SignUpRequestDTO requestDTO) {
		if (userRepository.existsByUsername(requestDTO.getUsername())) {
			throw new ApiException("Username already exists. Please Sign In");
		}

		if (userRepository.existsByEmail(requestDTO.getEmail())) {
			throw new ApiException("Email already Exists");

		}

		User user = new User(requestDTO.getUsername(), requestDTO.getEmail(),
				passwordEncoder.encode(requestDTO.getPassword()));

		Set<String> strRoles = requestDTO.getRole();

		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));

			roles.add(userRole);
		} else {

			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found"));

						roles.add(adminRole);
						break;

					case "seller":
						Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found"));

						roles.add(sellerRole);
						break;

					default:
						Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found"));

						roles.add(userRole);
						break;

				}
			});

		}

		user.setRoles(roles);
		userRepository.save(user);

		return new MessageResponseDTO("Signed Up Successfully!");

	}

	@Override
	public LoginResponseDTO getCurrentUsername(Authentication authentication) {
		LoginResponseDTO responseDTO = new LoginResponseDTO();

		if (authentication != null) {
			responseDTO.setUsername(authentication.getName());
			// responseDTO.setId(authentication.get);

			return responseDTO;

		} else {
			throw new UnauthorizedException();
		}

	}

	@Override
	public UserDetailsImpl getCurrentUserDetails(Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		if (userDetails != null) {
			return userDetails;
		} else {
			throw new UnauthorizedException();
		}
	}

	@Override
	public LoginResponseDTO signOut() {
		ResponseCookie cookie = jwtUtils.removeJwtCookie();

		LoginResponseDTO responseDTO = new LoginResponseDTO();

		responseDTO.setJwtCookie(cookie);

		return responseDTO;

	}

}
