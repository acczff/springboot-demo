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

    /** 按检验员查质检单（数据级权限：质检员只看自己的） */
    Page<InspectionOrder> findByInspectorId(Long inspectorId, Pageable pageable);

    /** 按检验员 + 状态查质检单 */
    Page<InspectionOrder> findByInspectorIdAndStatus(Long inspectorId, String status, Pageable pageable);
}
