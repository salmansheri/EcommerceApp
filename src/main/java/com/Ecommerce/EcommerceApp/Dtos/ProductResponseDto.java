package com.Ecommerce.EcommerceApp.Dtos;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto implements Serializable {

	private List<ProductDto> data;

	private Integer pageNumber;

	private Integer pageSize;

	private Integer totalElements;

	private Integer totalPages;

	private boolean lastPage;

}