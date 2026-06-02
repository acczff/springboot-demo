package com.zff.springboot_demo.stockout.repository;

import com.zff.springboot_demo.stockout.entity.StockOutOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockOutOrderRepository extends JpaRepository<StockOutOrder, Long> {
}
