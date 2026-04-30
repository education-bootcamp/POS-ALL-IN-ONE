package com.devstack.POS.service;

import com.devstack.POS.dto.request.CustomerOrderRequestDTO;

public interface CustomerOrderService {
    void createOrder(CustomerOrderRequestDTO dto);
}
