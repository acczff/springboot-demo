package com.zff.springboot_demo.inspection.repository;

import com.zff.springboot_demo.inspection.entity.InspectionOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InspectionOrderRepository extends JpaRepository<InspectionOrder, Long> {

    /** 按工单 ID 查所有质检单 */
    List<InspectionOrder> findByWorkOrderId(Long workOrderId);

    /** 按状态查质检单 */
    List<InspectionOrder> findByStatus(String status);

    Page<InspectionOrder> findByStatus(String status, Pageable pageable);
}
