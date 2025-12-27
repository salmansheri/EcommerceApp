package com.Ecommerce.EcommerceApp.Controllers;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Dtos.ProductResponseDto;
import com.Ecommerce.EcommerceApp.Interfaces.ProductService;
import com.Ecommerce.EcommerceApp.Lib.AppConstants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    // private final ProducttMapper producttMapper;

    @GetMapping("/public")
    public ResponseEntity<ProductResponseDto> getProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_PRODUCTS_DIR, required = false) String sortOrder) {
        ProductResponseDto productDtoList = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productDtoList, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProducts(@PathVariable Long id) {
        ProductDto productDto = productService.getProduct(id);

        return new ResponseEntity<>(productDto, HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<ProductResponseDto> getProductsByCategory(@PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_PRODUCTS_DIR, required = false) String sortOrder

    ) {
        ProductResponseDto productResponse = productService.getProductsByCategory(categoryId, pageNumber, pageSize,
                sortBy,
                sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/search")
    public ResponseEntity<ProductResponseDto> getProductsByKeyword(@RequestParam String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_PRODUCTS_DIR, required = false) String sortOrder

    ) {
        ProductResponseDto productResponse = productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @PostMapping("/admin/categories/{categoryId}")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto,
            @PathVariable Long categoryId) {
        ProductDto savedProductDto = productService.saveProduct(productDto, categoryId);

        return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);

    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        ProductDto updatedProductDto = productService.updateProduct(id, productDto);

        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);

    }

    @PutMapping("/public/{productId}/image")
    public ResponseEntity<ProductDto> updateProductImage(@PathVariable Long productId,
            @RequestParam("image") MultipartFile image) throws IOException {
        ProductDto updatedProductDto = productService.updateProductImage(productId, image);

        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);

    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
        ProductDto deletedProductDto = productService.deleteProduct(id);

        return new ResponseEntity<>(deletedProductDto, HttpStatus.OK);

    }

}
