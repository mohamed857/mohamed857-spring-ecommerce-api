package com.sc.SpringEcom.model.dto;

import java.util.List;

public record OrderRequest(
        String customName,
        String email,
        List<OrderItemRequest> items
) {}
