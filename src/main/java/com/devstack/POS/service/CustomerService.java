package com.devstack.POS.service;

import com.devstack.POS.dto.request.CustomerRequestDTO;
import com.devstack.POS.dto.response.CustomerResponseDTO;
import com.devstack.POS.dto.response.PagedResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    void createCustomer(CustomerRequestDTO dto);
    void updateCustomer(CustomerRequestDTO dto, UUID id);
    void deleteCustomer(UUID id);
    CustomerResponseDTO findCustomerById(UUID id);
    List<CustomerResponseDTO> findAll(); // not a best practice
    PagedResponseDTO<CustomerResponseDTO> searchCustomers(String searchText, int page, int size);
}
