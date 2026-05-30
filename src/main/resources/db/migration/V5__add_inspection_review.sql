-- Day130 不合格品评审：给 inspection_orders 加评审相关字段
ALTER TABLE inspection_orders
    ADD COLUMN reviewer_id    BIGINT       NULL COMMENT '评审人ID',
    ADD COLUMN reviewer_name  VARCHAR(50)  NULL COMMENT '评审人姓名快照',
    ADD COLUMN review_opinion VARCHAR(500) NULL COMMENT '评审意见',
    ADD COLUMN disposal       VARCHAR(20)  NULL COMMENT '处置方式：REWORK/CONCESSION/SCRAP',
    ADD COLUMN reviewed_at    DATETIME     NULL COMMENT '评审时间';
