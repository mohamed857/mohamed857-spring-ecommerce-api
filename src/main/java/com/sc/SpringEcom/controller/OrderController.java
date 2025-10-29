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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestParam OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrderResponse() {
        return new ResponseEntity<>(orderService.getAllOrderResponse(),HttpStatus.OK);
    }
}
