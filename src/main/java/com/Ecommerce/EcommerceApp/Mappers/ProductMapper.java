package com.Ecommerce.EcommerceApp.Mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.Ecommerce.EcommerceApp.Dtos.ProductDto;
import com.Ecommerce.EcommerceApp.Models.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    List<ProductDto> toDto(List<Product> product);

     Product toEntity(ProductDto productDto);

     void updateProductFromDto(ProductDto productDto, @MappingTarget Product product);

}
