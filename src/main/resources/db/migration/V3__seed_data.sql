-- 1. 插入菜单
-- 一级菜单: 系统管理 (sort: 1)
INSERT INTO menus (id, name, path, parent_id, sort, required_role) VALUES (1, '系统管理', NULL, NULL, 1, NULL);
-- 二级菜单: 用户管理 (sort: 1, path: '/users')
INSERT INTO menus (id, name, path, parent_id, sort, required_role) VALUES (2, '用户管理', '/users', 1, 1, NULL);
-- 二级菜单: 角色管理 (sort: 2, path: '/roles', required_role: 'ADMIN')
INSERT INTO menus (id, name, path, parent_id, sort, required_role) VALUES (3, '角色管理', '/roles', 1, 2, 'ADMIN');

-- 一级菜单: 生产管理 (sort: 2)
INSERT INTO menus (id, name, path, parent_id, sort, required_role) VALUES (4, '生产管理', NULL, NULL, 2, NULL);
-- 二级菜单: 工单管理 (sort: 1, path: '/workorders')
INSERT INTO menus (id, name, path, parent_id, sort, required_role) VALUES (5, '工单管理', '/workorders', 4, 1, NULL);
-- 二级菜单: 报工管理 (sort: 2, path: '/workreports')
INSERT INTO menus (id, name, path, parent_id, sort, required_role) VALUES (6, '报工管理', '/workreports', 4, 2, NULL);

-- 2. 插入角色
INSERT INTO roles (id, name, description) VALUES
(1, 'ADMIN', '超级管理员'),
(2, 'USER', '普通用户'),
(3, 'PLANNER', '计划员'),
(4, 'TEAM_LEADER', '班组长'),
(5, 'WORKER', '操作工');

-- 3. 插入权限点
INSERT INTO permissions (id, code, description) VALUES
(1, 'user:read', '查看用户'),
(2, 'user:create', '创建用户'),
(3, 'user:update', '编辑用户'),
(4, 'user:delete', '删除用户'),
(5, 'workorder:create', '创建工单'),
(6, 'workorder:issue', '下发工单'),
(7, 'workorder:read:all', '查看全部工单'),
(8, 'workorder:read:team', '查看本班组工单'),
(9, 'workorder:read:self', '查看自己的工单');

-- 4. 绑定角色-权限关系
-- ADMIN 拥有所有权限 (1-9)
INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9);
-- USER 拥有 user:read 和 workorder:read:self
INSERT INTO role_permissions (role_id, permission_id) VALUES
(2, 1), (2, 9);
-- PLANNER 拥有 workorder:create, workorder:issue, workorder:read:all
INSERT INTO role_permissions (role_id, permission_id) VALUES
(3, 5), (3, 6), (3, 7);
-- TEAM_LEADER 拥有 workorder:read:team
INSERT INTO role_permissions (role_id, permission_id) VALUES
(4, 8);
-- WORKER 拥有 workorder:read:self
INSERT INTO role_permissions (role_id, permission_id) VALUES
(5, 9);

-- 5. 插入种子用户 (密码均为 123456)
-- admin: $2a$10$KHFX/kCTgs6zOEosg4SEB.2PRmZZHqlKp7Vv5maskOSuLCkUGD72m
INSERT INTO users (id, username, password, email, create_time) VALUES
(1, 'admin', '$2a$10$KHFX/kCTgs6zOEosg4SEB.2PRmZZHqlKp7Vv5maskOSuLCkUGD72m', 'admin@example.com', 1711785600000);
-- zff: $2a$10$KHFX/kCTgs6zOEosg4SEB.2PRmZZHqlKp7Vv5maskOSuLCkUGD72m
INSERT INTO users (id, username, password, email, create_time) VALUES
(2, 'zff', '$2a$10$KHFX/kCTgs6zOEosg4SEB.2PRmZZHqlKp7Vv5maskOSuLCkUGD72m', 'zff@example.com', 1711785600000);

-- 6. 绑定用户-角色关系
-- admin -> ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
-- zff -> USER
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);

-- 7. 插入产品种子数据
INSERT INTO products (id, code, name, unit, created_time) VALUES
(1, 'P001', '标准椅子', '把', 1711785600000),
(2, 'P002', '办公桌',   '张', 1711785600000),
(3, 'P003', '文件柜',   '个', 1711785600000);
