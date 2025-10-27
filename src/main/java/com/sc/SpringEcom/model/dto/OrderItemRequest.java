package com.sc.SpringEcom.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) {}
