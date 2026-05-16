package com.zff.springboot_demo.workorder.service;

import com.zff.springboot_demo.workorder.entity.Product;
import com.zff.springboot_demo.workorder.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在"));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
