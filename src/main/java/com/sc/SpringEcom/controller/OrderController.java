package com.sc.SpringEcom.controller;

import com.sc.SpringEcom.model.Order;
import com.sc.SpringEcom.model.dto.OrderRequest;
import com.sc.SpringEcom.model.dto.OrderResponse;
import com.sc.SpringEcom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @GetMapping("/hi")
    public ResponseEntity< String> hi(){
        return new ResponseEntity<>("hi from OrederController", HttpStatus.OK);
    }

    @GetMapping("/placeOrder")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestParam OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(orderService.getAllOrderResponses(pageable),HttpStatus.OK);
    }
}
