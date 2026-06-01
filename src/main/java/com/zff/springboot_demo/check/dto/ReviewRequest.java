package com.zff.springboot_demo.check.dto;

/** 主管审核请求（reviewNote 可选，用于说明差异原因） */
public class ReviewRequest {

    private String reviewNote;

    public String getReviewNote() { return reviewNote; }
    public void setReviewNote(String reviewNote) { this.reviewNote = reviewNote; }
}
