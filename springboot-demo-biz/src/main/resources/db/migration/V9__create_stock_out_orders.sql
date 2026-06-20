CREATE TABLE stock_out_orders (
    id           BIGINT       NOT NULL AUTO_INCREMENT                        COMMENT '主键',
    status       VARCHAR(20)  NOT NULL DEFAULT 'PENDING'                     COMMENT '状态：PENDING/PICKING/PENDING_REVIEW/COMPLETED',
    created_by   BIGINT       NOT NULL                                       COMMENT '创建人（ADMIN用户ID）',
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP             COMMENT '创建时间',
    picked_by    BIGINT                                                      COMMENT '拣货人（用户ID，拣货完成后写入）',
    picked_at    DATETIME                                                    COMMENT '拣货完成时间',
    reviewed_by  BIGINT                                                      COMMENT '复核人（用户ID，复核完成后写入）',
    reviewed_at  DATETIME                                                    COMMENT '复核时间',
    review_note  VARCHAR(500)                                                COMMENT '复核备注',
    completed_at DATETIME                                                    COMMENT '出库完成时间（状态变COMPLETED时写入）',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单';

CREATE TABLE stock_out_items (
    id               BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_id         BIGINT NOT NULL                COMMENT '所属出库单ID',
    product_id       BIGINT NOT NULL                COMMENT '产品ID',
    planned_quantity INT    NOT NULL                COMMENT '计划出库数量（创建时定，库存扣减用这个值）',
    PRIMARY KEY (id),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库明细';
