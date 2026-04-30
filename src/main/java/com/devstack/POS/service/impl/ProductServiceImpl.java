package com.devstack.POS.service.impl;

import com.devstack.POS.dto.request.ProductRequestDTO;
import com.devstack.POS.dto.response.PagedResponseDTO;
import com.devstack.POS.dto.response.ProductResponseDTO;
import com.devstack.POS.entity.Product;
import com.devstack.POS.exception.EntryNotFoundException;
import com.devstack.POS.repo.ProductRepo;
import com.devstack.POS.service.ProductService;
import com.devstack.POS.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    @Override
    public void createProduct(ProductRequestDTO dto) {
        productRepo.save(productMapper.toProduct(dto));
    }

    @Override
    @Transactional
    public void updateProduct(ProductRequestDTO dto, UUID id) {
        Optional<Product> selectedProduct = productRepo.findById(id);
        if (selectedProduct.isEmpty()) {
            throw new EntryNotFoundException("Product Not Found");
        }
        Product product = selectedProduct.get();
        product.setDescription(dto.getDescription());
        product.setQtyOnHand(dto.getQtyOnHand());
        product.setUnitPrice(product.getUnitPrice());
        productRepo.save(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        /*Optional<Product> selectedProduct = productRepo.findById(id);
        if(selectedProduct.isEmpty()){
            throw new EntryNotFoundException("Product Not Found");
        }*/
        productRepo.deleteById(id);
    }

    @Override
    public ProductResponseDTO findProductById(UUID id) {
        Optional<Product> selectedProduct = productRepo.findById(id);
        if (selectedProduct.isEmpty()) {
            throw new EntryNotFoundException("Product Not Found");
        }
        return productMapper.toProductResponseDTO(selectedProduct.get());
    }

    @Override
    public PagedResponseDTO<ProductResponseDTO> searchProducts(String searchText, int page, int size) {
        String text = new StringBuilder().append("%").append(searchText).append("%").toString();
        return
                PagedResponseDTO.<ProductResponseDTO>builder()
                        .dataList(
                                productRepo.findAllProducts(text, PageRequest.of(page, size)).stream()
                                        .map(productMapper::toProductResponseDTO).toList()
                        )
                        .dataCount(productRepo.countAllProducts(text))
                        .build();

    }
}
