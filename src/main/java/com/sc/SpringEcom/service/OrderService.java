package com.sc.SpringEcom.service;

import com.sc.SpringEcom.model.Order;
import com.sc.SpringEcom.model.dto.OrderRequest;
import com.sc.SpringEcom.model.dto.OrderResponse;
import com.sc.SpringEcom.repo.OrderRepo;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    public Page<Order> getAllOrderResponses(Pageable pageable){
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
        OrderResponse orderResponse = new OrderResponse(
                order.getOrderId(),
                order.getCustomerName(),
                order.getEmail(),
                order.getStatus(),
                order.getOrderDate(),
                order.getOrderItems()
        );
        return  orderResponse;
    }
}
