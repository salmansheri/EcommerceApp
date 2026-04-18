package com.Ecommerce.EcommerceApp.Dtos;

import lombok.Data;

@Data
public class MessageResponseDTO {
	private Boolean success; 

	private String message;

	public MessageResponseDTO(Boolean success,String message) {
		this.success = success; 
		this.message = message;
	}

}
