package com.zff.springboot_demo.inspection.dto;

import jakarta.validation.constraints.NotBlank;

/** 不合格品评审请求体：必须填写评审意见和处置方式 */
public class InspectionOrderReviewRequest {

    @NotBlank(message = "评审意见不能为空")
    private String reviewOpinion;

    /** 处置方式：REWORK（返工）/ CONCESSION（让步接收）/ SCRAP（报废） */
    @NotBlank(message = "处置方式不能为空")
    private String disposal;

    public String getReviewOpinion() { return reviewOpinion; }
    public void setReviewOpinion(String reviewOpinion) { this.reviewOpinion = reviewOpinion; }

    public String getDisposal() { return disposal; }
    public void setDisposal(String disposal) { this.disposal = disposal; }
}
