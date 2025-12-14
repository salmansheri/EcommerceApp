package com.Ecommerce.EcommerceApp.Mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.Ecommerce.EcommerceApp.Dtos.CategoryDto;
import com.Ecommerce.EcommerceApp.Models.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {   
      
    CategoryDto toDto(Category category); 

       @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryDto categoryDto); 

    List<CategoryDto> toDto(List<Category> categories); 

     @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
     void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);
    
}
