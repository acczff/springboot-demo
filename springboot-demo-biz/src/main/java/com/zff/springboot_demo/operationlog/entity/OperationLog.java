package com.zff.springboot_demo.operationlog.entity;

import jakarta.persistence.*;

/**
 * 操作日志实体，记录操作人、动作、目标和执行结果。
 */
@Entity
@Table(name = "operation_logs")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // 日志ID（自增主键）

    @Column(name = "operator", length = 100)
    private String operator;    // 操作人用户名（从 token 解析后查库获得）

    @Column(name = "action", length = 100)
    private String action;      // 操作动作描述，来自 @LogOperation 注解的 value，如"新增角色"

    @Column(name = "target", length = 100)
    private String target;      // 操作目标类名，如"RoleController"（由切面自动读取）

    @Column(name = "target_id", length = 100)
    private String targetId;    // 操作目标的业务ID（可选，目前未填充）

    @Column(name = "result", length = 100)
    private String result;      // 操作结果，目前固定写 "success"（后续可扩展为 fail）

    @Column(name = "create_time")
    private Long createTime;    // 操作时间戳（毫秒）

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
