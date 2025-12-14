package com.Ecommerce.EcommerceApp.Services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Dtos.ProductResponseDto;
import com.Ecommerce.EcommerceApp.Exceptions.ApiException;
import com.Ecommerce.EcommerceApp.Exceptions.ResourceNotFoundException;
import com.Ecommerce.EcommerceApp.Interfaces.ProductService;
import com.Ecommerce.EcommerceApp.Lib.Utils;
import com.Ecommerce.EcommerceApp.Mappers.ProductMapper;
import com.Ecommerce.EcommerceApp.Models.Category;
import com.Ecommerce.EcommerceApp.Models.Product;
import com.Ecommerce.EcommerceApp.Repositories.ICategoryRepository;
import com.Ecommerce.EcommerceApp.Repositories.ProductRepository;


import lombok.RequiredArgsConstructor;

/**
 * Implementation of the ProductService interface for managing product operations.
 * This service handles CRUD operations for products, including pagination, sorting,
 * and associations with categories.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ICategoryRepository categoryRepository; 

    /**
     * Retrieves a paginated and sorted list of products.
     * @param pageNumber the page number (0-based)
     * @param pageSize the number of products per page
     * @param sortBy the field to sort by
     * @param sortOrder the sort order ("asc" or "desc")
     * @return ProductResponseDto containing the products and pagination metadata
     * @throws ApiException if no products are found
     */
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

    /**
     * Retrieves a product by its ID.
     * @param id the product ID
     * @return ProductDto of the found product
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return productMapper.toDto(product);
    }

    

    /**
     * Creates a new product and associates it with a category.
     * Calculates special price based on discount.
     * @param productDto the product data transfer object
     * @param categoryId the ID of the category to associate
     * @return ProductDto of the created product
     * @throws ResourceNotFoundException if the category is not found
     */
    @Override
    public ProductDto saveProduct(ProductDto productDto, Long categoryId) {
        Product product = productMapper.toEntity(productDto);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        product.setCategory(category);
        product.setImageUrl("something.png");

        Double discount = product.getDiscount() != null ? product.getDiscount() : 0.0; 

        Double specialPrice = product.getPrice() - ((discount * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);



        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    /**
     * Updates an existing product with new data.
     * @param id the product ID
     * @param productDto the updated product data
     * @return ProductDto of the updated product
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        existingProduct.setId(id);

        productMapper.updateProductFromDto(productDto, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toDto(updatedProduct);

    }

    /**
     * Deletes a product by its ID.
     * @param id the product ID
     * @return ProductDto of the deleted product
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public ProductDto deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.delete(existingProduct);

        return productMapper.toDto(existingProduct);

    }

    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);  

        List<ProductDto> productDtoList = productMapper.toDto(products); 

        return productDtoList; 
    }

    @Override
    public List<ProductDto> getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        
        List<Product> products = productRepository.findByNameLikeIgnoreCase('%' + keyword + '%');  

        List<ProductDto> productDtoList = productMapper.toDto(products); 

        return productDtoList; 
    }

	@Override
	public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {
		Product product  = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        // String path = "/images"; 
        String fileName = Utils.uploadImage(image);
        
        product.setImageUrl(fileName);

        Product updatedProduct = productRepository.save(product); 

        return productMapper.toDto(updatedProduct); 
	}

}
