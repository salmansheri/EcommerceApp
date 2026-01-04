package com.Ecommerce.EcommerceApp.Dtos;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto implements Serializable {

	private List<CategoryDto> data;

	private Integer pageNumber;

	private Integer pageSize;

	private Integer totalElements;

	private Integer totalPages;

	private boolean lastPage;

}
