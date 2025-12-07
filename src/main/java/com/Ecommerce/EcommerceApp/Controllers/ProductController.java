package com.Ecommerce.EcommerceApp.Controllers;


import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Dtos.ProductResponseDto;
import com.Ecommerce.EcommerceApp.Interfaces.ProductService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService; 
    // private final ProducttMapper producttMapper; 

    

    @GetMapping
    public ResponseEntity<ProductResponseDto> getProducts() {
        ProductResponseDto productDtoList = productService.getAllProducts();
        
       
        return new ResponseEntity<>(productDtoList, HttpStatus.OK); 

    }

     @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProducts(@PathVariable UUID id) {
        ProductDto productDto = productService.getProduct(id);
        
     
        return new ResponseEntity<>(productDto, HttpStatus.OK); 

    }

     @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProductDto = productService.saveProduct(productDto);
        
        
        return new ResponseEntity<>(savedProductDto, HttpStatus.OK); 

    }

         @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id, @RequestBody ProductDto productDto) {
        ProductDto updatedProductDto = productService.updateProduct(id, productDto);
        
        
        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK); 

    }

      @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable UUID id) {
        ProductDto deletedProductDto = productService.deleteProduct(id);
        
        
        return new ResponseEntity<>(deletedProductDto, HttpStatus.OK); 

    }




    
}
