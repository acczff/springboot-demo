package com.zff.springboot_demo.workorder.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 创建工单请求 DTO，接收前端传入的工单创建参数。
 * 仅包含前端需要填写的字段，其余字段（status/completedQty/createdTime）由后端自动设置。
 */
public class WorkOrderCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long createdBy;

    @NotEmpty
    @Valid
    private List<WorkOrderItemRequest> workOrderItemRequests;

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WorkOrderItemRequest> getWorkOrderItemRequests() {
        return workOrderItemRequests;
    }

    public void setWorkOrderItemRequests(List<WorkOrderItemRequest> workOrderItemRequests) {
        this.workOrderItemRequests = workOrderItemRequests;
    }

    public static class WorkOrderItemRequest{

        @NotNull
        private Long productId;

        @NotNull
        @Min(1)
        private Integer plannedQty;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getPlannedQty() {
            return plannedQty;
        }

        public void setPlannedQty(Integer plannedQty) {
            this.plannedQty = plannedQty;
        }
    }
}
