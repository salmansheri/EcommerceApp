package com.Ecommerce.EcommerceApp.Dtos;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDTO {

	@NotBlank
	@Size(min = 3)
	private String username;

	@NotBlank
	@Size()
	@Email
	private String email;

	private Set<String> role;

	@NotBlank
	@Size(min = 6, max = 30)
	private String password;

	public SignUpRequestDTO(String username, String email, Set<String> role, String password) {
		this.username = username;
		this.email = email;
		this.role = role;
		this.password = password;

	}

}
