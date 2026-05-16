package com.zff.springboot_demo.workreport.dto;

import jakarta.validation.constraints.NotNull;

public class WorkReportCreateRequest {

    @NotNull
    private Long workOrderId;

    @NotNull
    private Integer reportedQty;

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
}
