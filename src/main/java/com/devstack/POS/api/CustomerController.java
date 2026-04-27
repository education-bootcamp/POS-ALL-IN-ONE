package com.devstack.POS.api;

import com.devstack.POS.dto.request.CustomerRequestDTO;
import com.devstack.POS.dto.response.CustomerResponseDTO;
import com.devstack.POS.dto.response.PagedResponseDTO;
import com.devstack.POS.service.CustomerService;
import com.devstack.POS.util.StandardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<StandardResponseDTO> createCustomer(@RequestBody CustomerRequestDTO dto) {
        customerService.createCustomer(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponseDTO.builder()
                        .code(201)
                        .message("Customer created successfully")
                        .data(null)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponseDTO> updateCustomer(
            @RequestBody CustomerRequestDTO dto,
            @PathVariable UUID id) {
        customerService.updateCustomer(dto, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Customer updated successfully")
                        .data(null)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponseDTO> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Customer deleted successfully")
                        .data(null)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponseDTO> findCustomerById(@PathVariable UUID id) {
        CustomerResponseDTO customer = customerService.findCustomerById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Customer retrieved successfully")
                        .data(customer)
                        .build());
    }

    @GetMapping
    public ResponseEntity<StandardResponseDTO> searchCustomers(
            @RequestParam(defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponseDTO<CustomerResponseDTO> result = customerService.searchCustomers(searchText, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Customers retrieved successfully")
                        .data(result)
                        .build());
    }

    @GetMapping
    public ResponseEntity<StandardResponseDTO> searchCustomers() {
        List<CustomerResponseDTO> result = customerService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Customers retrieved successfully")
                        .data(result)
                        .build());
    }
}
