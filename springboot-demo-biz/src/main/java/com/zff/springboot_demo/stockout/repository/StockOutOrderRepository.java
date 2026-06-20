package com.zff.springboot_demo.stockout.repository;

import com.zff.springboot_demo.stockout.entity.StockOutOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockOutOrderRepository extends JpaRepository<StockOutOrder, Long> {

    List<StockOutOrder> findByStatus(String status);
}
