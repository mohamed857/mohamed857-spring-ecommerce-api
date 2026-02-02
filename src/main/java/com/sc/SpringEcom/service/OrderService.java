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

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;

    public OrderService(OrderRepo orderRepo, ProductRepo productRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    public Page<OrderResponse> getAllOrderResponsePageable(Pageable pageable){
        return orderRepo.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest i: orderRequest.items()){
            Product product = productRepo.findById(i.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStockQuantity() < i.quantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }
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
        return  mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrderResponse(){
        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order :orders){
            orderResponses.add(mapToResponse(order));
        }
        return orderResponses;
    }

    public OrderResponse getOrderById(int id){
        Order order = orderRepo.findById(id).orElseThrow(
                ()-> new RuntimeException( "order not found with id "+ id));
        return  mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem orderItem :order.getOrderItems()){
            OrderItemResponse oIR = new OrderItemResponse(
                    orderItem.getProduct().getName(),
                    orderItem.getQuantity(),
                    orderItem.getTotalPrice()
            );
            itemResponses.add(oIR);
        }
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerName(order.getCustomerName())
                .email(order.getEmail())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .items(itemResponses)
                .build();
    }

    public OrderResponse updateOrder(int id, OrderRequest request){
        Order order =orderRepo.findById(id).orElseThrow(
                ()->new RuntimeException("Order not found with id: " + id)
        );
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());

        Order updatedOrder = orderRepo.save(order);
        return mapToResponse(updatedOrder);
    }
    public void deleteOrder(int id){
        if (!orderRepo.existsById(id))
            throw  new RuntimeException("order not found by id "+ id);
        orderRepo.deleteById(id);
    }
    public OrderResponse updateOrderStatus(int id, String status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        order.setStatus(status.toUpperCase());

        Order updatedOrder = orderRepo.save(order);
        return mapToResponse(updatedOrder);
    }
}


