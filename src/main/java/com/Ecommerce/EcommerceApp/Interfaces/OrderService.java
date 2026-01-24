package com.Ecommerce.EcommerceApp.Interfaces;

import com.Ecommerce.EcommerceApp.Dtos.OrderDTO;
import com.Ecommerce.EcommerceApp.Dtos.OrderRequestDTO;

public interface OrderService {

    OrderDTO placeOrder(String emailId, Long addressId, OrderRequestDTO requestDTO, String paymentMethod, String pgName, String pgPaymentId,
            String pgStatus, String pgResponseMessage);
    
}
