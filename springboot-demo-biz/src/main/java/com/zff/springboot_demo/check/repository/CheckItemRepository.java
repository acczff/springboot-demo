package com.zff.springboot_demo.check.repository;

import com.zff.springboot_demo.check.entity.CheckItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckItemRepository extends JpaRepository<CheckItem, Long> {

    /** 查某个盘点计划下的全部明细 */
    List<CheckItem> findByPlanId(Long planId);
}
