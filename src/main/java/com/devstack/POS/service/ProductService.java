package com.devstack.POS.service;


import com.devstack.POS.dto.request.ProductRequestDTO;
import com.devstack.POS.dto.response.PagedResponseDTO;
import com.devstack.POS.dto.response.ProductResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    void createProduct(ProductRequestDTO dto);
    void updateProduct(ProductRequestDTO dto, UUID id);
    void deleteProduct(UUID id);
    ProductResponseDTO findProductById(UUID id);
    PagedResponseDTO<ProductResponseDTO> searchProducts(String searchText, int page, int size);
}
