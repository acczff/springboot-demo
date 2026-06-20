package com.zff.springboot_demo.stockin.dto;

import jakarta.validation.constraints.NotBlank;

/** 库管员上架：填库位 */
public class ShelveRequest {

    @NotBlank(message = "库位不能为空")
    private String location;

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
