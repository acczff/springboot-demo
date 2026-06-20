-- =============================================
-- MES 工单模块初始化脚本
-- =============================================

-- 1. 产品表
CREATE TABLE IF NOT EXISTS products (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    code         VARCHAR(50)  NOT NULL UNIQUE COMMENT '产品编码',
    name         VARCHAR(100) NOT NULL        COMMENT '产品名称',
    unit         VARCHAR(20)  NOT NULL        COMMENT '单位（个/箱/吨）',
    created_time BIGINT       NOT NULL        COMMENT '创建时间（毫秒时间戳）'
);

-- 2. 工单表
CREATE TABLE IF NOT EXISTS work_orders (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id    BIGINT       NOT NULL        COMMENT '关联产品ID',
    planned_qty   INT          NOT NULL        COMMENT '计划数量',
    completed_qty INT          NOT NULL DEFAULT 0 COMMENT '已完成数量',
    status        VARCHAR(20)  NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT/ISSUED/IN_PROGRESS/COMPLETED/CANCELLED',
    created_by    BIGINT       NOT NULL        COMMENT '创建人user_id',
    created_time  BIGINT       NOT NULL        COMMENT '创建时间（毫秒时间戳）',
    FOREIGN KEY (product_id)  REFERENCES products(id),
    FOREIGN KEY (created_by)  REFERENCES users(id)
);

-- 3. 新增角色
INSERT IGNORE INTO roles (name, description) VALUES
('PLANNER',     '计划员'),
('TEAM_LEADER', '班组长'),
('WORKER',      '操作工');

-- 4. 新增权限码
INSERT IGNORE INTO permissions (code, description) VALUES
('workorder:create',    '创建工单'),
('workorder:issue',     '下发工单'),
('workorder:read:all',  '查看全部工单'),
('workorder:read:team', '查看本班组工单'),
('workorder:read:self', '查看自己的工单');

-- 5. 绑定角色权限
-- 计划员：创建+下发+查看全部
INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'PLANNER'
  AND p.code IN ('workorder:create', 'workorder:issue', 'workorder:read:all');

-- 班组长：查看本班组
INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'TEAM_LEADER'
  AND p.code IN ('workorder:read:team');

-- 操作工：只看自己的
INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'WORKER'
  AND p.code IN ('workorder:read:self');

-- 6. 插入产品样例数据
INSERT IGNORE INTO products (code, name, unit, created_time) VALUES
('P001', '标准椅子', '把', UNIX_TIMESTAMP() * 1000),
('P002', '办公桌',   '张', UNIX_TIMESTAMP() * 1000),
('P003', '文件柜',   '个', UNIX_TIMESTAMP() * 1000);