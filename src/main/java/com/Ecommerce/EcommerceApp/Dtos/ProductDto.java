package com.Ecommerce.EcommerceApp.Dtos;


import java.util.UUID;


import com.Ecommerce.EcommerceApp.Models.Category;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private UUID id;
    private String name;
    private String description;
    private Integer quantity;
    private Double price;
    private Double specialPrice;

    private Category category;

}
