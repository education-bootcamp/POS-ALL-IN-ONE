package com.devstack.POS.util;


import com.devstack.POS.dto.request.ProductRequestDTO;
import com.devstack.POS.dto.response.ProductResponseDTO;
import com.devstack.POS.entity.Product;
import com.devstack.POS.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toProduct(ProductRequestDTO dto) {
        if (dto == null) throw new ValidationException("DTO Not Found");
        return Product.builder().description(dto.getDescription())
                .qtyOnHand(dto.getQtyOnHand()).unitPrice(dto.getUnitPrice()).build();
    }

    public ProductResponseDTO toProductResponseDTO(Product product) {
        if (product == null) throw new ValidationException("Product Entity Not Found");
        return ProductResponseDTO.builder()
                .id(product.getId())
                .description(product.getDescription())
                .qtyOnHand(product.getQtyOnHand())
                .unitPrice(product.getUnitPrice())
                .build();
    }
}
