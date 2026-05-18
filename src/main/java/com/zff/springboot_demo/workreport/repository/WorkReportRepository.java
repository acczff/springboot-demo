package com.zff.springboot_demo.workreport.repository;

import com.zff.springboot_demo.workreport.entity.WorkReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface WorkReportRepository extends JpaRepository<WorkReport, Long> {

    /** 按工单 ID 分页查询（主管视角：查某张工单下所有报工记录，排除已软删除） */
    Page<WorkReport> findByWorkOrderIdAndDeletedFalse(Long workOrderId, Pageable pageable);

    /** 按工单 ID 全量查询（级联软删除专用，不分页） */
    List<WorkReport> findByWorkOrderIdAndDeletedFalse(Long workOrderId);

    /** 按报工人 ID 分页查询（工人视角：查我自己的全部报工记录，排除已软删除） */
    Page<WorkReport> findByReportedByAndDeletedFalse(Long reportedBy, Pageable pageable);

    /** 按状态分页查询（质检视角：查待审核列表，传 "SUBMITTED"，排除已软删除） */
    Page<WorkReport> findByStatusAndDeletedFalse(String status, Pageable pageable);

}
