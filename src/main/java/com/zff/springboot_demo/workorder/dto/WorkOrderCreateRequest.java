package com.zff.springboot_demo.workorder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 创建工单请求 DTO，接收前端传入的工单创建参数。
 * 仅包含前端需要填写的字段，其余字段（status/completedQty/createdTime）由后端自动设置。
 */
public class WorkOrderCreateRequest {

    /** 要生产的产品 ID，不能为空 */
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    /**
     * 计划生产数量，至少为 1。
     * 注意：必须用 Integer（包装类型）而非 int（基本类型），@NotNull 才能生效。
     * int 永远有默认值 0，不可能为 null，所以 @NotNull 加在 int 上无效。
     */
    @NotNull(message = "计划数量不能为空")
    @Min(value = 1, message = "计划数量至少为1")
    private Integer plannedQty;

    /** 创建人用户ID，不能为空 */
    @NotNull(message = "创建人不能为空")
    private Long createdBy;

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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
