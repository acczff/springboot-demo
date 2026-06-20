package com.zff.springboot_demo.inspection.dto;

import jakarta.validation.constraints.NotBlank;

/** 不合格判定请求体：必须填写不合格原因 */
public class InspectionOrderFailRequest {

    @NotBlank(message = "不合格原因不能为空")
    private String failReason;

    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }
}
