CREATE TABLE inventory_check_plans (
    id          BIGINT       NOT NULL AUTO_INCREMENT           COMMENT '主键',
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING'        COMMENT '状态：PENDING/IN_PROGRESS/PENDING_REVIEW/COMPLETED',
    created_by  BIGINT       NOT NULL                          COMMENT '创建人（用户ID）',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    reviewed_by BIGINT                                         COMMENT '审核人（用户ID，审核后才有值）',
    reviewed_at DATETIME                                       COMMENT '审核时间',
    review_note VARCHAR(500)                                   COMMENT '审核备注',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点计划';

CREATE TABLE check_items (
    id              BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    plan_id         BIGINT NOT NULL                COMMENT '所属盘点计划ID',
    product_id      BIGINT NOT NULL                COMMENT '产品ID',
    system_quantity INT    NOT NULL                COMMENT '账面数（计划创建时从inventory快照）',
    actual_quantity INT                            COMMENT '实盘数（盘点员填，未盘时为NULL）',
    difference      INT                            COMMENT '差异=actual-system（提交时系统写入）',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点明细';