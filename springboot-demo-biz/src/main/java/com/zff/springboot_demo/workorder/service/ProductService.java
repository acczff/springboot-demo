package com.zff.springboot_demo.workorder.service;

import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.stockin.repository.InventoryRepository;
import com.zff.springboot_demo.stockin.repository.StockInOrderRepository;
import com.zff.springboot_demo.stockout.repository.StockOutItemRepository;
import com.zff.springboot_demo.workorder.dto.ProductRequest;
import com.zff.springboot_demo.workorder.entity.Product;
import com.zff.springboot_demo.workorder.repository.ProductRepository;
import com.zff.springboot_demo.workorder.repository.WorkOrderItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final WorkOrderItemRepository workOrderItemRepository;
    private final InventoryRepository inventoryRepository;
    private final StockInOrderRepository stockInOrderRepository;
    private final StockOutItemRepository stockOutItemRepository;

    public ProductService(ProductRepository productRepository,
                          WorkOrderItemRepository workOrderItemRepository,
                          InventoryRepository inventoryRepository,
                          StockInOrderRepository stockInOrderRepository,
                          StockOutItemRepository stockOutItemRepository) {
        this.productRepository = productRepository;
        this.workOrderItemRepository = workOrderItemRepository;
        this.inventoryRepository = inventoryRepository;
        this.stockInOrderRepository = stockInOrderRepository;
        this.stockOutItemRepository = stockOutItemRepository;
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "产品不存在"));
    }

    public Product findByCode(String code) {
        return productRepository.findByCode(normalize(code))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "产品不存在"));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return productRepository.findAll(pageable);
        }
        String normalizedKeyword = keyword.trim();
        return productRepository.findByCodeContainingOrNameContaining(
                normalizedKeyword, normalizedKeyword, pageable);
    }

    public Product create(ProductRequest request) {
        String code = normalize(request.getCode());
        if (productRepository.existsByCode(code)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "产品编码已存在");
        }
        Product product = new Product();
        product.setCode(code);
        product.setName(normalize(request.getName()));
        product.setUnit(normalize(request.getUnit()));
        product.setCreatedTime(System.currentTimeMillis());
        return productRepository.save(product);
    }

    public Product update(Long id, ProductRequest request) {
        Product product = findById(id);
        String code = normalize(request.getCode());
        if (productRepository.existsByCodeAndIdNot(code, id)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "产品编码已存在");
        }
        product.setCode(code);
        product.setName(normalize(request.getName()));
        product.setUnit(normalize(request.getUnit()));
        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = findById(id);
        if (isReferenced(id)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "产品已被业务单据或库存引用，不能删除");
        }
        productRepository.delete(product);
    }

    private boolean isReferenced(Long productId) {
        return workOrderItemRepository.existsByProductId(productId)
                || inventoryRepository.existsByProductId(productId)
                || stockInOrderRepository.existsByProductId(productId)
                || stockOutItemRepository.existsByProductId(productId);
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
