-- 质检单表
-- 状态流：PENDING（待检）→ INSPECTING（检验中）→ PASS（合格）/ FAIL（不合格）
CREATE TABLE inspection_orders (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    work_order_id    BIGINT       NOT NULL               COMMENT '关联工单 ID',
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING/INSPECTING/PASS/FAIL',
    inspector_id     BIGINT                              COMMENT '检验人 ID',
    inspector_name   VARCHAR(50)                         COMMENT '检验人姓名快照',
    fail_reason      VARCHAR(500)                        COMMENT '不合格原因（FAIL 时填写）',
    inspected_at     DATETIME                            COMMENT '检验完成时间（PASS/FAIL 时填写）',
    created_by       BIGINT       NOT NULL               COMMENT '创建人 ID',
    created_by_name  VARCHAR(50)  NOT NULL               COMMENT '创建人姓名快照',
    created_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_work_order_id (work_order_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质检单';
