package com.Ecommerce.EcommerceApp.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.EcommerceApp.Dtos.OrderDTO;
import com.Ecommerce.EcommerceApp.Dtos.OrderRequestDTO;
import com.Ecommerce.EcommerceApp.Interfaces.OrderService;
import com.Ecommerce.EcommerceApp.Lib.Utils.AuthUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthUtils authUtils;

    @PostMapping("/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod,
            @RequestBody OrderRequestDTO requestDTO) {
        String emailId = authUtils.loggedInEmail();
        OrderDTO orderDTO = orderService.placeOrder(emailId, requestDTO.getAddressId(), requestDTO, paymentMethod,
                requestDTO.getPgName(),
                requestDTO.getPgPaymentId(), requestDTO.getPgStatus(), requestDTO.getPgResponseMessage());

        return new ResponseEntity<OrderDTO>(orderDTO, HttpStatus.CREATED);

    }

}
