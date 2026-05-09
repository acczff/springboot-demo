package com.zff.springboot_demo.operationlog.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "operation_logs")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operator", length = 100)
    private String operator;

    @Column(name = "action", length = 100)
    private String action;

    @Column(name = "target", length = 100)
    private String target;

    @Column(name = "target_id", length = 100)
    private String targetId;

    @Column(name = "result", length = 100)
    private String result;

    @Column(name = "create_time")
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
