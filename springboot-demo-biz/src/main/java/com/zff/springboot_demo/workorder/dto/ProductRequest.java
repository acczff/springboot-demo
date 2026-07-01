package com.zff.springboot_demo.workorder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductRequest {

    @NotBlank(message = "产品编码不能为空")
    @Size(max = 50, message = "产品编码不能超过50个字符")
    private String code;

    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称不能超过100个字符")
    private String name;

    @NotBlank(message = "计量单位不能为空")
    @Size(max = 20, message = "计量单位不能超过20个字符")
    private String unit;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
