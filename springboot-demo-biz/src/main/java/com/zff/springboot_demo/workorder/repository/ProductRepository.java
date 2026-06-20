package com.zff.springboot_demo.workorder.repository;

import com.zff.springboot_demo.workorder.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
