package com.zff.springboot_demo.repair.dto;

import jakarta.validation.constraints.NotBlank;

/** 报修请求 */
public class CreateRepairRequest {

    @NotBlank(message = "设备编号不能为空")
    private String equipmentCode;

    @NotBlank(message = "故障描述不能为空")
    private String faultDescription;

    public String getEquipmentCode() { return equipmentCode; }
    public void setEquipmentCode(String equipmentCode) { this.equipmentCode = equipmentCode; }

    public String getFaultDescription() { return faultDescription; }
    public void setFaultDescription(String faultDescription) { this.faultDescription = faultDescription; }
}
