-- Day135 设备维修工单表
-- 状态流：PENDING（待派单）→ ASSIGNED（已派单）→ IN_PROGRESS（维修中）
--       → PENDING_CONFIRM（待确认）→ CLOSED（已关闭）
CREATE TABLE repair_orders (
    id                   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    equipment_code       VARCHAR(100) NOT NULL                COMMENT '设备编号',
    fault_description    TEXT         NOT NULL                COMMENT '故障描述（操作工填）',
    status               VARCHAR(20)  NOT NULL DEFAULT 'PENDING' COMMENT '工单状态',
    reporter_id          BIGINT       NOT NULL                COMMENT '发现人ID（操作工）',
    supervisor_id        BIGINT                               COMMENT '维修主管ID',
    repairer_id          BIGINT                               COMMENT '维修员ID',
    repair_result        TEXT                                 COMMENT '维修记录（维修员填）',
    confirmation_opinion TEXT                                 COMMENT '确认记录（操作工填）',
    created_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报修时间',
    assigned_at          DATETIME                             COMMENT '派单时间',
    started_at           DATETIME                             COMMENT '开始维修时间',
    completed_at         DATETIME                             COMMENT '维修完成时间',
    confirmed_at         DATETIME                             COMMENT '确认时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备维修工单';