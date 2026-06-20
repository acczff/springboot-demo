package com.zff.springboot_demo.check.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/** 创建盘点计划请求：主管传入要盘点的产品ID列表 */
public class CreateCheckPlanRequest {

    @NotEmpty(message = "产品列表不能为空")
    private List<Long> productIds;

    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
}
