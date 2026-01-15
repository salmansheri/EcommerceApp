package com.Ecommerce.EcommerceApp.Exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Unauthorized. Please Sign in"); 

    }
    
}
