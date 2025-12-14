package com.Ecommerce.EcommerceApp.Repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ecommerce.EcommerceApp.Models.Category;
import com.Ecommerce.EcommerceApp.Models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>  {
    List<Product> findByCategoryOrderByPriceAsc(Category category);
    List<Product> findByNameLikeIgnoreCase(String keyword);  
    
}
