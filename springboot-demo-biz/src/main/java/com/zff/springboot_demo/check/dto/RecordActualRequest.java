package com.zff.springboot_demo.check.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** 盘点员录入实盘数量请求 */
public class RecordActualRequest {

    @NotNull(message = "实盘数量不能为空")
    @Min(value = 0, message = "实盘数量不能为负数")
    private Integer actualQuantity;

    public Integer getActualQuantity() { return actualQuantity; }
    public void setActualQuantity(Integer actualQuantity) { this.actualQuantity = actualQuantity; }
}
