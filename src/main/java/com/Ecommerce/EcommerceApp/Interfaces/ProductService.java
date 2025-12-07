package com.Ecommerce.EcommerceApp.Interfaces;

import java.util.UUID;

import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Dtos.ProductResponseDto;

public interface ProductService {
    ProductResponseDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDto getProduct(UUID id);

    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(UUID id, ProductDto productDto);

    ProductDto deleteProduct(UUID id);

}
