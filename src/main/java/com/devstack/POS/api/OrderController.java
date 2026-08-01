package com.devstack.POS.api;

import com.devstack.POS.dto.request.CustomerOrderRequestDTO;
import com.devstack.POS.dto.response.OrderResponseDTO;
import com.devstack.POS.dto.response.PagedResponseDTO;
import com.devstack.POS.service.CustomerOrderService;
import com.devstack.POS.util.StandardResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {
    private final CustomerOrderService customerOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<StandardResponseDTO> createOrder(@Valid @RequestBody CustomerOrderRequestDTO dto) {
        customerOrderService.createOrder(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponseDTO.builder()
                        .code(201)
                        .message("Order created successfully")
                        .data(null)
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<StandardResponseDTO> updateOrder(
            @Valid @RequestBody CustomerOrderRequestDTO dto,
            @PathVariable UUID id) {
        customerOrderService.updateOrder(dto, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Order updated successfully")
                        .data(null)
                        .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<StandardResponseDTO> deleteOrder(@PathVariable UUID id) {
        customerOrderService.deleteOrder(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Order deleted successfully")
                        .data(null)
                        .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<StandardResponseDTO> findOrderById(@PathVariable UUID id) {
        OrderResponseDTO order = customerOrderService.findOrderById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Order retrieved successfully")
                        .data(order)
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    public ResponseEntity<StandardResponseDTO> searchOrders(
            @RequestParam(defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponseDTO<OrderResponseDTO> result = customerOrderService.searchOrders(searchText, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Orders retrieved successfully")
                        .data(result)
                        .build());
    }
}