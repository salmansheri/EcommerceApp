package com.Ecommerce.EcommerceApp.Dtos;

import java.io.Serializable;

import com.Ecommerce.EcommerceApp.Models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

	private Long id;

	private String name;

	private String description;

	private Integer quantity;

	private Double price;

	private Double specialPrice;

	private String imageUrl;

	private Double discount;

	private User user;

	// private Category category;

}
