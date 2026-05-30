package com.zff.springboot_demo.repair.dto;

import jakarta.validation.constraints.NotBlank;

/** 维修员填写维修记录 */
public class CompleteRepairRequest {

    @NotBlank(message = "维修记录不能为空")
    private String repairResult;

    public String getRepairResult() { return repairResult; }
    public void setRepairResult(String repairResult) { this.repairResult = repairResult; }
}
