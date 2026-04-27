package com.devstack.POS.service.impl;

import com.devstack.POS.dto.request.CustomerRequestDTO;
import com.devstack.POS.dto.response.CustomerResponseDTO;
import com.devstack.POS.dto.response.PagedResponseDTO;
import com.devstack.POS.entity.Customer;
import com.devstack.POS.exception.EntryNotFoundException;
import com.devstack.POS.repo.CustomerRepo;
import com.devstack.POS.service.CustomerService;
import com.devstack.POS.util.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceIMPL implements CustomerService {

    private final CustomerRepo customerRepo;
    private final CustomerMapper customerMapper;

    @Override
    public void createCustomer(CustomerRequestDTO dto) {
        customerRepo.save(customerMapper.toCustomer(dto));
    }

    @Override
    public void updateCustomer(CustomerRequestDTO dto, UUID id) {
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new EntryNotFoundException("Customer not found for provided id"));

        customer.setName(dto.getName());
        customer.setAddress(dto.getAddress());
        customer.setSalary(dto.getSalary());
        customerRepo.save(customer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        customerRepo.deleteById(id);
    }

    @Override
    public CustomerResponseDTO findCustomerById(UUID id) {
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new EntryNotFoundException("Customer not found for provided id"));
        return customerMapper.toCustomerResponseDTO(customer);
    }

    @Override
    public List<CustomerResponseDTO> findAll() {
        return customerRepo.findAll().stream().map(customerMapper::toCustomerResponseDTO).toList();
    }

    @Override
    public PagedResponseDTO<CustomerResponseDTO> searchCustomers(String searchText, int page, int size) {
        searchText= "%"+searchText+"%";
        return PagedResponseDTO.<CustomerResponseDTO>builder()
                .dataList(
                       customerRepo.findAllCustomers(searchText, PageRequest.of(page, size))
                               .stream().map(customerMapper::toCustomerResponseDTO).toList())
                .dataCount(
                        customerRepo.countAllCustomers(searchText)
                ).build();
    }
}
