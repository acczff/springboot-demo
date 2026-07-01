INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '产品管理', '/products', id, 3, NULL FROM menus
WHERE name = '生产管理'
  AND NOT EXISTS (SELECT 1 FROM menus WHERE path = '/products');

INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '质检管理', NULL, NULL, 3, NULL
WHERE NOT EXISTS (SELECT 1 FROM menus WHERE name = '质检管理' AND parent_id IS NULL);

INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '质检单管理', '/inspection-orders', id, 1, NULL FROM menus
WHERE name = '质检管理'
  AND NOT EXISTS (SELECT 1 FROM menus WHERE path = '/inspection-orders');

INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '库存管理', NULL, NULL, 4, NULL
WHERE NOT EXISTS (SELECT 1 FROM menus WHERE name = '库存管理' AND parent_id IS NULL);

INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '库存查询', '/inventory', id, 1, NULL FROM menus
WHERE name = '库存管理'
  AND NOT EXISTS (SELECT 1 FROM menus WHERE path = '/inventory');

INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '入库管理', '/stock-in-orders', id, 2, NULL FROM menus
WHERE name = '库存管理'
  AND NOT EXISTS (SELECT 1 FROM menus WHERE path = '/stock-in-orders');

INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '出库管理', '/stock-out-orders', id, 3, NULL FROM menus
WHERE name = '库存管理'
  AND NOT EXISTS (SELECT 1 FROM menus WHERE path = '/stock-out-orders');

INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '盘点管理', '/check-plans', id, 4, NULL FROM menus
WHERE name = '库存管理'
  AND NOT EXISTS (SELECT 1 FROM menus WHERE path = '/check-plans');

INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '设备维修', NULL, NULL, 5, NULL
WHERE NOT EXISTS (SELECT 1 FROM menus WHERE name = '设备维修' AND parent_id IS NULL);

INSERT INTO menus (name, path, parent_id, sort, required_role)
SELECT '维修工单', '/repair-orders', id, 1, NULL FROM menus
WHERE name = '设备维修'
  AND NOT EXISTS (SELECT 1 FROM menus WHERE path = '/repair-orders');

INSERT INTO permissions (code, description)
SELECT 'product:read', '查看产品'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'product:read');
INSERT INTO permissions (code, description)
SELECT 'product:create', '创建产品'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'product:create');
INSERT INTO permissions (code, description)
SELECT 'product:update', '更新产品'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'product:update');
INSERT INTO permissions (code, description)
SELECT 'product:delete', '删除产品'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'product:delete');

INSERT INTO permissions (code, description)
SELECT 'inspection:manage', '管理质检单'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'inspection:manage');
INSERT INTO permissions (code, description)
SELECT 'stockin:manage', '管理入库单'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'stockin:manage');
INSERT INTO permissions (code, description)
SELECT 'stockout:manage', '管理出库单'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'stockout:manage');
INSERT INTO permissions (code, description)
SELECT 'inventory:read', '查看库存'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'inventory:read');
INSERT INTO permissions (code, description)
SELECT 'check:manage', '管理盘点'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'check:manage');
INSERT INTO permissions (code, description)
SELECT 'repair:manage', '管理维修工单'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'repair:manage');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.code IN (
    'product:read', 'product:create', 'product:update', 'product:delete',
    'inspection:manage', 'stockin:manage', 'stockout:manage',
    'inventory:read', 'check:manage', 'repair:manage'
)
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1 FROM role_permissions rp
      WHERE rp.role_id = r.id AND rp.permission_id = p.id
  );

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.code IN ('product:read', 'inventory:read')
WHERE r.name IN ('PLANNER', 'TEAM_LEADER', 'WORKER', 'USER')
  AND NOT EXISTS (
      SELECT 1 FROM role_permissions rp
      WHERE rp.role_id = r.id AND rp.permission_id = p.id
  );
