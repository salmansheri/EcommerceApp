package com.Ecommerce.EcommerceApp.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Ecommerce.EcommerceApp.Dtos.CategoryDto;
import com.Ecommerce.EcommerceApp.Dtos.CategoryResponseDto;
import com.Ecommerce.EcommerceApp.Exceptions.ApiException;
import com.Ecommerce.EcommerceApp.Exceptions.ResourceNotFoundException;
import com.Ecommerce.EcommerceApp.Interfaces.ICategoryService;
import com.Ecommerce.EcommerceApp.Mappers.CategoryMapper;
import com.Ecommerce.EcommerceApp.Models.Category;
import com.Ecommerce.EcommerceApp.Repositories.ICategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    // private List<Category> categories = new ArrayList<>();

    private final ICategoryRepository categoryRepo;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending(); 
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> categoryPage = categoryRepo.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();

        if (categories.isEmpty() || categories.size() == 0)
            throw new ApiException("No Category created till now");

        List<CategoryDto> categoryDtoList = categoryMapper.toDto(categories);

        return new CategoryResponseDto(
                categoryDtoList,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                (int) categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast());

    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // category.setId(nextId++);

        if (categoryDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category cannot be null");
        }

        Category existingCategory = categoryRepo.findByName(categoryDto.getName());

        if (existingCategory != null) {
            throw new ApiException("Category already exists!");
        }

        Category category = categoryMapper.toEntity(categoryDto);

        Category savedCategory = categoryRepo.save(category);
        CategoryDto saveCategoryDto = categoryMapper.toDto(savedCategory);

        return saveCategoryDto;
    }

    @Override
    public CategoryDto deleteCategory(Long categoryId) {

        Category category = categoryRepo.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "Id", categoryId));

        categoryRepo.delete(category);

        return categoryMapper.toDto(category);

    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto updatedCategoryDto) {

        if (updatedCategoryDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Cannot be null");
        }

        Category existingCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));

        updatedCategoryDto.setId(categoryId);

        categoryMapper.updateCategoryFromDto(updatedCategoryDto, existingCategory);

        Category updatedCategory = categoryRepo.save(existingCategory);

        return categoryMapper.toDto(updatedCategory);

    }

}
