package com.devstack.POS.api;

import com.devstack.POS.dto.request.ProductRequestDTO;
import com.devstack.POS.dto.response.ProductResponseDTO;
import com.devstack.POS.dto.response.PagedResponseDTO;
import com.devstack.POS.service.ProductService;
import com.devstack.POS.service.ProductService;
import com.devstack.POS.util.StandardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<StandardResponseDTO> createProduct(@RequestBody ProductRequestDTO dto) {
        productService.createProduct(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponseDTO.builder()
                        .code(201)
                        .message("Product created successfully")
                        .data(null)
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<StandardResponseDTO> updateProduct(
            @RequestBody ProductRequestDTO dto,
            @PathVariable UUID id) {
        productService.updateProduct(dto, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Product updated successfully")
                        .data(null)
                        .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponseDTO> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(StandardResponseDTO.builder()
                        .code(204)
                        .message("Product deleted successfully")
                        .data(null)
                        .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<StandardResponseDTO> findProductById(@PathVariable UUID id) {
        ProductResponseDTO Product = productService.findProductById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Product retrieved successfully")
                        .data(Product)
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<StandardResponseDTO> searchProducts(
            @RequestParam(defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponseDTO<ProductResponseDTO> result = productService.searchProducts(searchText, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponseDTO.builder()
                        .code(200)
                        .message("Products retrieved successfully")
                        .data(result)
                        .build());
    }
}
