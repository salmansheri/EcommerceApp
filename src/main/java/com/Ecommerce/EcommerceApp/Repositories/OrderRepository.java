package com.Ecommerce.EcommerceApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ecommerce.EcommerceApp.Models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    
} 
