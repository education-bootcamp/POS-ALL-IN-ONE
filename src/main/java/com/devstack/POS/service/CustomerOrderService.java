package com.devstack.POS.service;

import com.devstack.POS.dto.request.CustomerOrderRequestDTO;
import com.devstack.POS.dto.response.OrderResponseDTO;
import com.devstack.POS.dto.response.PagedResponseDTO;

import java.util.UUID;

public interface CustomerOrderService {
    void createOrder(CustomerOrderRequestDTO dto);
    void updateOrder(CustomerOrderRequestDTO dto, UUID id);
    void deleteOrder(UUID id);
    OrderResponseDTO findOrderById(UUID id);
    PagedResponseDTO<OrderResponseDTO> searchOrders(String searchText, int page, int size);
}
