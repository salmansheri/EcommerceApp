package com.Ecommerce.EcommerceApp.Exceptions;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ApiException() {
		super("Something Went Wrong. Please Try Again!"); 

	}

	public ApiException(String message) {
		super(message);
	}

}
