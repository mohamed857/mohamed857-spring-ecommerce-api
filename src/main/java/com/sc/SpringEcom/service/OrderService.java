package com.sc.SpringEcom.service;

import com.sc.SpringEcom.model.Order;
import com.sc.SpringEcom.model.OrderItem;
import com.sc.SpringEcom.model.Product;
import com.sc.SpringEcom.model.dto.OrderItemRequest;
import com.sc.SpringEcom.model.dto.OrderItemResponse;
import com.sc.SpringEcom.model.dto.OrderRequest;
import com.sc.SpringEcom.model.dto.OrderResponse;
import com.sc.SpringEcom.repo.OrderRepo;
import com.sc.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ProductRepo productRepo;

    public Page<Order> getAllOrder(Pageable pageable){
        return orderRepo.findAll(pageable);
    }

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(orderRequest.customName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest i: orderRequest.items()){
            Product product = productRepo.findById(i.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setStockQuantity(product.getStockQuantity()- i.quantity());
            productRepo.save(product);
            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(i.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(i.quantity())))
                    .order(order)
                    .build();
            orderItems.add(item);
        }
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem oI:savedOrder.getOrderItems()){
            OrderItemResponse item = new OrderItemResponse(
                    oI.getProduct().getName(),
                    oI.getQuantity(),
                    oI.getTotalPrice()
            );
            itemResponses.add(item);
        }


        OrderResponse orderResponse = new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                itemResponses
        );
        return  orderResponse;
    }

    @Transactional
    public List<OrderResponse> getAllOrderResponse(){
        List<Order> orders = orderRepo.findAll();

        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order :orders){
            List<OrderItemResponse> itemResponses = new ArrayList<>();
            for (OrderItem orderItem :order.getOrderItems()){
                OrderItemResponse oIR = new OrderItemResponse(
                        orderItem.getProduct().getName(),
                        orderItem.getQuantity(),
                        orderItem.getTotalPrice()
                );
                itemResponses.add(oIR);
            }
            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponses
            );
            orderResponses.add(orderResponse);

        }

        return orderResponses;
    }

}


