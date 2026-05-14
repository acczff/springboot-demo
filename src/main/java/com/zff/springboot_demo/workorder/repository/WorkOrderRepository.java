package com.zff.springboot_demo.workorder.repository;

import com.zff.springboot_demo.workorder.entity.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 工单数据访问层，封装工单表查询。
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {


    //通过状态筛选
    Page<WorkOrder> findByStatus(String status, Pageable pageable);
}
