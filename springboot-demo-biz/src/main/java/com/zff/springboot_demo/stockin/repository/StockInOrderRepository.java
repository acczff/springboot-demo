package com.zff.springboot_demo.stockin.repository;

import com.zff.springboot_demo.stockin.entity.StockInOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockInOrderRepository extends JpaRepository<StockInOrder, Long> {

    Page<StockInOrder> findByStatus(String status, Pageable pageable);

    Page<StockInOrder> findByCreatedBy(Long createdBy, Pageable pageable);

    Page<StockInOrder> findByCreatedByAndStatus(Long createdBy, String status, Pageable pageable);

    boolean existsByProductId(Long productId);
}
