package com.zff.springboot_demo.repair.dto;

import jakarta.validation.constraints.NotBlank;

/** 操作工确认维修结果 */
public class ConfirmRepairRequest {

    @NotBlank(message = "确认记录不能为空")
    private String confirmationOpinion;

    public String getConfirmationOpinion() { return confirmationOpinion; }
    public void setConfirmationOpinion(String confirmationOpinion) { this.confirmationOpinion = confirmationOpinion; }
}
