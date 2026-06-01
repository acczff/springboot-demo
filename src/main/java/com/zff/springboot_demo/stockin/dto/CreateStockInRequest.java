package com.zff.springboot_demo.stockin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** 库管员创建入库单 */
public class CreateStockInRequest {

    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @NotNull(message = "入库数量不能为空")
    @Min(value = 1, message = "入库数量必须大于0")
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
