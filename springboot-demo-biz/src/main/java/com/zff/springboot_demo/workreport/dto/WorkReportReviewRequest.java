package com.zff.springboot_demo.workreport.dto;

/**
 * 审核请求 DTO。
 * 通过时 rejectReason 可为空；驳回时 rejectReason 必填。
 */
public class WorkReportReviewRequest {

    /** 驳回原因（仅驳回时必填，通过时可不传） */
    private String rejectReason;

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
