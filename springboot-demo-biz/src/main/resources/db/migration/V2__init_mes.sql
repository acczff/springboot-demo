CREATE TABLE IF NOT EXISTS products (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '产品ID',
    code         VARCHAR(50)  NOT NULL UNIQUE      COMMENT '产品编码',
    name         VARCHAR(100) NOT NULL             COMMENT '产品名称',
    unit         VARCHAR(20)  NOT NULL             COMMENT '单位',
    created_time BIGINT       NOT NULL             COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

CREATE TABLE IF NOT EXISTS work_orders (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    product_id      BIGINT       NOT NULL             COMMENT '关联产品ID',
    planned_qty     INT          NOT NULL             COMMENT '计划数量',
    completed_qty   INT          NOT NULL DEFAULT 0   COMMENT '已完成数量',
    status          VARCHAR(20)  NOT NULL DEFAULT 'DRAFT' COMMENT '状态',
    created_by      BIGINT       NOT NULL             COMMENT '创建人user_id',
    created_time    BIGINT       NOT NULL             COMMENT '创建时间（毫秒）',
    name            VARCHAR(100) NOT NULL             COMMENT '工单名称',
    created_by_name VARCHAR(50)                       COMMENT '创建人姓名快照',
    deleted         BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '是否软删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';

CREATE TABLE IF NOT EXISTS work_order_items (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细主键',
    work_order_id BIGINT NOT NULL                   COMMENT '关联工单ID',
    product_id    BIGINT NOT NULL                   COMMENT '产品ID',
    planned_qty   INT    NOT NULL                   COMMENT '计划数量',
    completed_qty INT    NOT NULL DEFAULT 0         COMMENT '已完成数量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单明细表';

CREATE TABLE IF NOT EXISTS work_reports (
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报工ID',
    work_order_id      BIGINT      NOT NULL               COMMENT '关联工单ID',
    reported_qty       INT         NOT NULL               COMMENT '本次报工数量',
    reported_by        BIGINT      NOT NULL               COMMENT '报工人ID',
    reported_by_name   VARCHAR(50)                        COMMENT '报工人姓名快照',
    reported_time      BIGINT      NOT NULL               COMMENT '报工时间（毫秒）',
    status             VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT '状态',
    reviewed_by        BIGINT                             COMMENT '审核人ID',
    reviewed_by_name   VARCHAR(50)                        COMMENT '审核人姓名快照',
    reviewed_time      BIGINT                             COMMENT '审核时间（毫秒）',
    reject_reason      VARCHAR(200)                       COMMENT '驳回原因',
    deleted            BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '是否软删除',
    work_order_item_id BIGINT                             COMMENT '关联明细行ID',
    product_name       VARCHAR(100)                       COMMENT '产品名称快照'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报工记录表';
