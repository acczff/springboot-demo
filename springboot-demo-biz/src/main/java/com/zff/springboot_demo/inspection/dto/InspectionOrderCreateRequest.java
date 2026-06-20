package com.zff.springboot_demo.inspection.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 创建质检单请求体。
 * createdBy 从 token 自动注入，不在此 DTO 中。
 */
public class InspectionOrderCreateRequest {

    /** 关联工单 ID（必填） */
    @NotNull(message = "工单 ID 不能为空")
    private Long workOrderId;

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
}
