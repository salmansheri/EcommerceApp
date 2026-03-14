package com.Ecommerce.EcommerceApp.Services;

import java.io.IOException;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Ecommerce.EcommerceApp.Dtos.CartDTO;
import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Dtos.ProductResponseDto;
import com.Ecommerce.EcommerceApp.Exceptions.ApiException;
import com.Ecommerce.EcommerceApp.Exceptions.ResourceNotFoundException;
import com.Ecommerce.EcommerceApp.Interfaces.CartService;
import com.Ecommerce.EcommerceApp.Interfaces.ProductService;
import com.Ecommerce.EcommerceApp.Lib.Util;
import com.Ecommerce.EcommerceApp.Lib.Utils.AuthUtils;
import com.Ecommerce.EcommerceApp.Mappers.CartMapper;
import com.Ecommerce.EcommerceApp.Mappers.ProductMapper;
import com.Ecommerce.EcommerceApp.Models.Cart;
import com.Ecommerce.EcommerceApp.Models.Category;
import com.Ecommerce.EcommerceApp.Models.Product;
import com.Ecommerce.EcommerceApp.Repositories.CartRepository;
import com.Ecommerce.EcommerceApp.Repositories.ICategoryRepository;
import com.Ecommerce.EcommerceApp.Repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the ProductService interface for managing product
 * operations. This
 * service handles CRUD operations for products, including pagination, sorting,
 * and
 * associations with categories.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final AuthUtils authUtils;

	private final ProductRepository productRepository;

	private final ProductMapper productMapper;

	private final ICategoryRepository categoryRepository;

	private final CartService cartService;

	private final CartRepository cartRepository;

	private final CartMapper cartMapper;

   

	/**
	 * Retrieves a paginated and sorted list of products.
	 * 
	 * @param pageNumber the page number (0-based)
	 * @param pageSize   the number of products per page
	 * @param sortBy     the field to sort by
	 * @param sortOrder  the sort order ("asc" or "desc")
	 * @return ProductResponseDto containing the products and pagination metadata
	 * @throws ApiException if no products are found
	 */
	@Override
	@Cacheable(value = "ecommerce::productList", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder}", unless = "#result.data.size() == 0")
	public ProductResponseDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Product> productPage = productRepository.findAll(pageDetails);
		List<Product> products = productPage.getContent();

		// if (products.isEmpty() || products.size() == 0)
		// throw new ApiException("No Product created till now");

		List<ProductDto> productDtoList = productMapper.toDto(products);
		return new ProductResponseDto(productDtoList, productPage.getNumber(), productPage.getSize(),
				(int) productPage.getTotalElements(), productPage.getTotalPages(), productPage.isLast()

		);

	}

	/**
	 * Retrieves a product by its ID.
	 * 
	 * @param id the product ID
	 * @return ProductDto of the found product
	 * @throws ResourceNotFoundException if the product is not found
	 */
	@Override
	@Cacheable(value = "ecommerce::productById", key = "#id")
	public ProductDto getProduct(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

		return productMapper.toDto(product);
	}

	/**
	 * Creates a new product and associates it with a category. Calculates special
	 * price
	 * based on discount.
	 * 
	 * @param productDto the product data transfer object
	 * @param categoryId the ID of the category to associate
	 * @return ProductDto of the created product
	 * @throws ResourceNotFoundException if the category is not found
	 */
	@Override
	@CacheEvict(value = "ecommerce::productList", allEntries = true)
	public ProductDto saveProduct(ProductDto productDto, Long categoryId) {
		log.info("Save Product Called....................");
		
		Product product = productMapper.toEntity(productDto);
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

		boolean isProductExist = false;

		List<Product> products = category.getProducts();

		for (Product value : products) {
			if (value.getName().equals(productDto.getName())) {
				isProductExist = true;
				break;
			}
		}

		if (!isProductExist) {
			product.setCategory(category);
			product.setUser(authUtils.loggedInUser()); 
			// product.setImageUrl("something.png");

			Double discount = product.getDiscount() != null ? product.getDiscount() : 0.0;

			Double specialPrice = product.getPrice() - ((discount * 0.01) * product.getPrice());
			product.setSpecialPrice(specialPrice);
		

			Product savedProduct = productRepository.save(product);

			return productMapper.toDto(savedProduct);

		} else {
			throw new ApiException("Product already exist");
		}

	}

	/**
	 * Updates an existing product with new data.
	 * 
	 * @param id         the product ID
	 * @param productDto the updated product data
	 * @return ProductDto of the updated product
	 * @throws ResourceNotFoundException if the product is not found
	 */
	@Override
	@Caching(evict = { @CacheEvict(value = "ecommerce::productById", key = "#id"),
			@CacheEvict(value = "ecommerce::productList", allEntries = true)

	})
	public ProductDto updateProduct(Long id, ProductDto productDto) {
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

		existingProduct.setProductId(id);

		productMapper.updateProductFromDto(productDto, existingProduct);

		Product updatedProduct = productRepository.save(existingProduct);

		List<Cart> carts = cartRepository.findCartsByProductId(id);

		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = cartMapper.toDTO(cart);
			List<ProductDto> productDtos = cart.getCartItems().stream().map(ci -> productMapper.toDto(ci.getProduct()))
					.toList();
			cartDTO.setProducts(productDtos);
			return cartDTO;
		}).toList();

		cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), id));

		return productMapper.toDto(updatedProduct);

	}

	/**
	 * Deletes a product by its ID.
	 * 
	 * @param id the product ID
	 * @return ProductDto of the deleted product
	 * @throws ResourceNotFoundException if the product is not found
	 */
	@Override
	// @CacheEvict(value = "ecommerce::productById", key = "#id")
	@Caching(evict = { @CacheEvict(value = "ecommerce::productById", key = "#id"),
			@CacheEvict(value = "ecommerce::productList", allEntries = true),
			@CacheEvict(value = "ecommerce::productListByCategory", allEntries = true),

	})
	public ProductDto deleteProduct(Long id) {
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

		List<Cart> carts = cartRepository.findCartsByProductId(id);

		carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), id));

		productRepository.delete(existingProduct);

		return productMapper.toDto(existingProduct);

	}

	@Override
	@Cacheable(value = "ecommerce::productListByCategory", key = "{#categoryId, #pageNumber, #pageSize, #sortBy, #sortOrder}", unless = "#result.data.size() == 0")
	public ProductResponseDto getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
		// List<Product> products =
		// productRepository.findByCategoryOrderByPriceAsc(category);
		Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

		List<Product> products = productPage.getContent();

		List<ProductDto> productDtoList = productMapper.toDto(products);

		return new ProductResponseDto(

				productDtoList, productPage.getNumber(), productPage.getSize(), (int) productPage.getTotalElements(),
				productPage.getTotalPages(), productPage.isLast()

		);
	}

	@Override
	// @Cacheable(
	// value = "ecommerce::productList",
	// key = "{#pageNumber, #pageSize, #sortBy, #sortOrder}",
	// unless = "#result.data.size() == 0"
	// )
	public ProductResponseDto getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		// Category category = categoryRepository.findById(categoryId)
		// .orElseThrow(() -> new ResourceNotFoundException("Category", "id",
		// categoryId));
		// List<Product> products =
		// productRepository.findByCategoryOrderByPriceAsc(category);
		Page<Product> productPage = productRepository.findByNameLikeIgnoreCase("%" + keyword + "%", pageDetails);

		List<Product> products = productPage.getContent();

		List<ProductDto> productDtoList = productMapper.toDto(products);

		return new ProductResponseDto(

				productDtoList, productPage.getNumber(), productPage.getSize(), (int) productPage.getTotalElements(),
				productPage.getTotalPages(), productPage.isLast()

		);
	}

	@Override
	@CacheEvict(value = "ecommerce::productList", allEntries = true)
	public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		// String path = "/images";
		String fileName = Util.uploadImage(image);

		product.setImageUrl(fileName);

		Product updatedProduct = productRepository.save(product);

		return productMapper.toDto(updatedProduct);
	}

}
