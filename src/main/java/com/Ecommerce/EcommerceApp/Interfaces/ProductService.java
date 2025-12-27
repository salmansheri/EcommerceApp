package com.Ecommerce.EcommerceApp.Interfaces;

import java.io.IOException;


import org.springframework.web.multipart.MultipartFile;

import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Dtos.ProductResponseDto;

public interface ProductService {
    ProductResponseDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDto getProduct(Long id);

    ProductDto saveProduct(ProductDto productDto, Long categoryId);

    ProductDto updateProduct(Long id, ProductDto productDto);

    ProductDto deleteProduct(Long id);

    ProductResponseDto getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    ProductResponseDto getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException;

}
