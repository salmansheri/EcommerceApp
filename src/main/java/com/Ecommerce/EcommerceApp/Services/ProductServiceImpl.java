package com.Ecommerce.EcommerceApp.Services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Dtos.ProductResponseDto;
import com.Ecommerce.EcommerceApp.Exceptions.ApiException;
import com.Ecommerce.EcommerceApp.Exceptions.ResourceNotFoundException;
import com.Ecommerce.EcommerceApp.Interfaces.ProductService;
import com.Ecommerce.EcommerceApp.Mappers.ProductMapper;
import com.Ecommerce.EcommerceApp.Models.Product;
import com.Ecommerce.EcommerceApp.Repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> products = productPage.getContent();

        if (products.isEmpty() || products.size() == 0)
            throw new ApiException("No Product created till now");

        List<ProductDto> productDtoList = productMapper.toDto(products);
        return new ProductResponseDto(
                productDtoList,
                productPage.getNumber(),
                productPage.getSize(),
                (int) productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()

        );

    }

    @Override
    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return productMapper.toDto(product);
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);

        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productMapper.updateProductFromDto(productDto, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toDto(updatedProduct);

    }

    @Override
    public ProductDto deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.delete(existingProduct);

        return productMapper.toDto(existingProduct);

    }

}
