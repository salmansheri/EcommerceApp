package com.Ecommerce.EcommerceApp.Controllers;

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

import com.Ecommerce.EcommerceApp.Dtos.CategoryDto;
import com.Ecommerce.EcommerceApp.Dtos.CategoryResponseDto;
import com.Ecommerce.EcommerceApp.Interfaces.ICategoryService;
import com.Ecommerce.EcommerceApp.Lib.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponseDto> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required=false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_CATEGORIES_DIR, required = false) String sortOrder

    ) {
        CategoryResponseDto categoryResponseDto = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategoryDto = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategoryDto, HttpStatus.CREATED);

    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDto> DeleteCategory(@PathVariable Long categoryId) {

        CategoryDto categoryDto = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);

    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @PathVariable Long categoryId,
            @RequestBody CategoryDto updatedCategory) {

        CategoryDto categoryDto = categoryService.updateCategory(categoryId, updatedCategory);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);

    }

}
