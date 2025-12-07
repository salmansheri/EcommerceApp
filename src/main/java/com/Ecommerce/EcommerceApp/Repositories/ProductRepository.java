package com.Ecommerce.EcommerceApp.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ecommerce.EcommerceApp.Models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>  {
    
}
