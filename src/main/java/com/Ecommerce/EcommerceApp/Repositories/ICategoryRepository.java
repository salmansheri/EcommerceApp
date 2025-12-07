package com.Ecommerce.EcommerceApp.Repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ecommerce.EcommerceApp.Models.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name); 

}
