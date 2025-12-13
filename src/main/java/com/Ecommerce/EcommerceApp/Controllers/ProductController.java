package com.Ecommerce.EcommerceApp.Controllers;




import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Dtos.ProductResponseDto;
import com.Ecommerce.EcommerceApp.Interfaces.ProductService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService; 
    // private final ProducttMapper producttMapper; 

    

    @GetMapping("/public")
    public ResponseEntity<ProductResponseDto> getProducts(
        @RequestParam(name = "pageNumber", defaultValue = "0", required=false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = "10", required=false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = "name", required=false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = "asc", required=false) String sortOrder
    ) {
        ProductResponseDto productDtoList = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        
       
        return new ResponseEntity<>(productDtoList, HttpStatus.OK); 

    }

     @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProducts(@PathVariable Long id) {
        ProductDto productDto = productService.getProduct(id);
        
     
        return new ResponseEntity<>(productDto, HttpStatus.OK); 

    }

     @PostMapping("/admin/categories/{categoryId}")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, @PathVariable Long categoryId) {
        ProductDto savedProductDto = productService.saveProduct(productDto, categoryId);
        
        
        return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED); 

    }

         @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        ProductDto updatedProductDto = productService.updateProduct(id, productDto);
        
        
        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK); 

    }

      @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
        ProductDto deletedProductDto = productService.deleteProduct(id);
        
        
        return new ResponseEntity<>(deletedProductDto, HttpStatus.OK); 

    }




    
}
