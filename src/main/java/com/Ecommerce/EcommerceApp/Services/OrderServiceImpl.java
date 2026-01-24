package com.Ecommerce.EcommerceApp.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Ecommerce.EcommerceApp.Dtos.OrderDTO;
import com.Ecommerce.EcommerceApp.Dtos.OrderRequestDTO;
import com.Ecommerce.EcommerceApp.Exceptions.ApiException;
import com.Ecommerce.EcommerceApp.Exceptions.ResourceNotFoundException;
import com.Ecommerce.EcommerceApp.Interfaces.CartService;
import com.Ecommerce.EcommerceApp.Interfaces.OrderService;
import com.Ecommerce.EcommerceApp.Mappers.OrderItemMapper;
import com.Ecommerce.EcommerceApp.Mappers.OrderMapper;
import com.Ecommerce.EcommerceApp.Models.Address;
import com.Ecommerce.EcommerceApp.Models.Cart;
import com.Ecommerce.EcommerceApp.Models.CartItem;
import com.Ecommerce.EcommerceApp.Models.Order;
import com.Ecommerce.EcommerceApp.Models.OrderItem;
import com.Ecommerce.EcommerceApp.Models.Payment;
import com.Ecommerce.EcommerceApp.Models.Product;
import com.Ecommerce.EcommerceApp.Repositories.AddressRepository;
import com.Ecommerce.EcommerceApp.Repositories.CartRepository;
import com.Ecommerce.EcommerceApp.Repositories.OrderItemRepository;
import com.Ecommerce.EcommerceApp.Repositories.OrderRepository;
import com.Ecommerce.EcommerceApp.Repositories.PaymentRepository;
import com.Ecommerce.EcommerceApp.Repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, OrderRequestDTO requestDTO, String paymentMethod,
            String pgName,
            String pgPaymentId, String pgStatus, String pgResponseMessage) {

        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "Email", emailId);
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));

        Order order = new Order();

        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted!");

        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);

        payment.setOrder(order);

        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new ApiException("Cannot find cartitems");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);

        }

        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();

            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - quantity);

            productRepository.save(product);

            cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());

        });

        OrderDTO orderDTO = orderMapper.toDto(savedOrder);

        orderItems.forEach(item -> {
            orderDTO.getOrderItems().add(orderItemMapper.toDto(item));
        });


        orderDTO.setAddressId(addressId);

        return orderDTO;

    }

}
