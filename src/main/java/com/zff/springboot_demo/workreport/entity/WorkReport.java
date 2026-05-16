package com.zff.springboot_demo.workreport.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "work_reports")
public class WorkReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_order_id", nullable = false)
    private Long workOrderId;

    @Column(name = "reported_qty", nullable = false)
    private Integer reportedQty;

    @Column(name = "reported_by", nullable = false)
    private Long reportedBy;

    @Column(name = "reported_by_name", length = 50)
    private String reportedByName;

    @Column(name = "reported_time", nullable = false)
    private Long reportedTime;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "DRAFT";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Integer getReportedQty() {
        return reportedQty;
    }

    public void setReportedQty(Integer reportedQty) {
        this.reportedQty = reportedQty;
    }

    public Long getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Long reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public Long getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(Long reportedTime) {
        this.reportedTime = reportedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
