package com.Ecommerce.EcommerceApp.Interfaces;


import com.Ecommerce.EcommerceApp.Dtos.CategoryDto;
import com.Ecommerce.EcommerceApp.Dtos.CategoryResponseDto;

public interface ICategoryService {
    CategoryResponseDto getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDto createCategory(CategoryDto category);

    CategoryDto deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long categoryId, CategoryDto updatedCategory);

}
