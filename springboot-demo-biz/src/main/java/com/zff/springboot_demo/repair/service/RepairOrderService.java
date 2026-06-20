package com.zff.springboot_demo.repair.service;

import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.repair.entity.RepairOrder;
import com.zff.springboot_demo.repair.repository.RepairOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RepairOrderService {

    private final RepairOrderRepository repairOrderRepository;

    public RepairOrderService(RepairOrderRepository repairOrderRepository) {
        this.repairOrderRepository = repairOrderRepository;
    }

    /** 查详情 */
    public RepairOrder findById(Long id) {
        return repairOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "维修工单不存在"));
    }

    /** 操作工报修：创建 PENDING 维修单 */
    public RepairOrder create(String equipmentCode, String faultDescription, Long reporterId) {
        RepairOrder order = new RepairOrder();
        order.setEquipmentCode(equipmentCode);
        order.setFaultDescription(faultDescription);
        order.setReporterId(reporterId);
        order.setCreatedAt(LocalDateTime.now());
        return repairOrderRepository.save(order);
    }

    /** 主管派单：PENDING → ASSIGNED */
    public RepairOrder assign(Long id, Long supervisorId, Long repairerId) {
        RepairOrder order = findById(id);
        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有待派单的维修单才能派单");
        }
        order.setStatus("ASSIGNED");
        order.setSupervisorId(supervisorId);
        order.setRepairerId(repairerId);
        order.setAssignedAt(LocalDateTime.now());
        return repairOrderRepository.save(order);
    }

    /** 维修员接单/开始维修：ASSIGNED → IN_PROGRESS */
    public RepairOrder start(Long id, Long repairerId) {
        RepairOrder order = findById(id);
        if (!"ASSIGNED".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有已派单的维修单才能开始维修");
        }
        order.setStatus("IN_PROGRESS");
        order.setStartedAt(LocalDateTime.now());
        return repairOrderRepository.save(order);
    }

    /** 维修员完成维修：IN_PROGRESS → PENDING_CONFIRM */
    public RepairOrder complete(Long id, Long repairerId, String repairResult) {
        RepairOrder order = findById(id);
        if (!"IN_PROGRESS".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有维修中的维修单才能完成维修");
        }
        order.setStatus("PENDING_CONFIRM");
        order.setRepairResult(repairResult);
        order.setCompletedAt(LocalDateTime.now());
        return repairOrderRepository.save(order);
    }

    /** 操作工确认：PENDING_CONFIRM → CLOSED */
    public RepairOrder confirm(Long id, Long reporterId, String confirmationOpinion) {
        RepairOrder order = findById(id);
        if (!"PENDING_CONFIRM".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有待确认的维修单才能确认");
        }
        order.setStatus("CLOSED");
        order.setConfirmationOpinion(confirmationOpinion);
        order.setConfirmedAt(LocalDateTime.now());
        return repairOrderRepository.save(order);
    }

    /**
     * 按状态分页查询，数据级权限。
     * ADMIN 看全部；维修员只看自己的。
     */
    public Page<RepairOrder> findByStatus(String status, Pageable pageable, Long currentUserId, boolean isAdmin) {
        boolean hasStatus = status != null && !status.isBlank();
        if (isAdmin) {
            return hasStatus
                    ? repairOrderRepository.findByStatus(status, pageable)
                    : repairOrderRepository.findAll(pageable);
        } else {
            return hasStatus
                    ? repairOrderRepository.findByRepairerIdAndStatus(currentUserId, status, pageable)
                    : repairOrderRepository.findByRepairerId(currentUserId, pageable);
        }
    }

    /** 按维修员ID查（供 Controller 非分页场景使用） */
    public Page<RepairOrder> findByRepairerId(Long repairerId, Pageable pageable) {
        return repairOrderRepository.findByRepairerId(repairerId, pageable);
    }
}
