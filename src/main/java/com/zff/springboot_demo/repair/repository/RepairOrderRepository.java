package com.zff.springboot_demo.repair.repository;

import com.zff.springboot_demo.repair.entity.RepairOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {

    /** 按状态分页查 */
    Page<RepairOrder> findByStatus(String status, Pageable pageable);

    /** 按维修员ID查 */
    Page<RepairOrder> findByRepairerId(Long repairerId, Pageable pageable);

    /** 按维修员ID + 状态查 */
    Page<RepairOrder> findByRepairerIdAndStatus(Long repairerId, String status, Pageable pageable);
}
