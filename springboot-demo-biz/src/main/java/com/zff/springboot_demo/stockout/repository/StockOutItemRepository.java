package com.zff.springboot_demo.stockout.repository;

import com.zff.springboot_demo.stockout.entity.StockOutItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockOutItemRepository extends JpaRepository<StockOutItem, Long> {

    List<StockOutItem> findByOrderId(Long orderId);

    boolean existsByProductId(Long productId);
}
