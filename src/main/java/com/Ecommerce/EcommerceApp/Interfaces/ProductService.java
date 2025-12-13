package com.Ecommerce.EcommerceApp.Interfaces;



import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Dtos.ProductResponseDto;

public interface ProductService {
    ProductResponseDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDto getProduct(Long id);

    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    ProductDto deleteProduct(Long id);

}
