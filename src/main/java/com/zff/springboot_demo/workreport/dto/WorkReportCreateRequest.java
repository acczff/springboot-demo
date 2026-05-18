package com.zff.springboot_demo.workreport.dto;

import jakarta.validation.constraints.NotNull;

public class WorkReportCreateRequest {

    @NotNull
    private Long workOrderId;

    @NotNull
    private Integer reportedQty;

    /** 可选：按产品明细报工时填写 */
    private Long workOrderItemId;

    /** 产品名称快照，按产品明细报工时填写 */
    private String productName;

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Integer getReportedQty() {
        return reportedQty;
    }

    public void setReportedQty(Integer reportedQty) {
        this.reportedQty = reportedQty;
    }

    public Long getWorkOrderItemId() {
        return workOrderItemId;
    }

    public void setWorkOrderItemId(Long workOrderItemId) {
        this.workOrderItemId = workOrderItemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
