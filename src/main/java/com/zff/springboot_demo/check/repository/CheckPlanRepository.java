package com.zff.springboot_demo.check.repository;

import com.zff.springboot_demo.check.entity.InventoryCheckPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckPlanRepository extends JpaRepository<InventoryCheckPlan, Long> {
}
