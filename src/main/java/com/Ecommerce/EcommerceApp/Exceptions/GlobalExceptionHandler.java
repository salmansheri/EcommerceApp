package com.Ecommerce.EcommerceApp.Exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Ecommerce.EcommerceApp.Dtos.ApiResponseDto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>(); 

        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField(); 
            String message = err.getDefaultMessage(); 
            response.put(fieldName, message); 
        });

        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST); 

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> customResourceNotFoundException(ResourceNotFoundException ex) {
        String message = ex.getMessage(); 
        ApiResponseDto apiResponseDto = new ApiResponseDto(message, false); 

        return new ResponseEntity<>(apiResponseDto, HttpStatus.NOT_FOUND); 

    }

     @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseDto> customApiException(ApiException ex) {
        String message = ex.getMessage(); 
        ApiResponseDto apiResponseDto = new ApiResponseDto(message, false); 


        return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST); 

    }
}
