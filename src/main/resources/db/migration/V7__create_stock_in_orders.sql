CREATE TABLE stock_in_orders (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    product_id  BIGINT      NOT NULL                COMMENT '产品ID',
    quantity    INT         NOT NULL                COMMENT '入库数量',
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING_RECEIVE' COMMENT '状态',
    created_by  BIGINT      NOT NULL                COMMENT '库管员ID',
    created_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    received_at DATETIME                            COMMENT '验收时间',
    shelved_at  DATETIME                            COMMENT '上架时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单';

CREATE TABLE inventory (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    product_id  BIGINT      NOT NULL                COMMENT '产品ID',
    location    VARCHAR(100)                        COMMENT '库位（如 A区-3排-2格）',
    quantity    INT         NOT NULL DEFAULT 0      COMMENT '当前库存数量',
    created_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '首次入库时间',
    updated_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_product_id (product_id) COMMENT '每个产品只有一条库存记录'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';