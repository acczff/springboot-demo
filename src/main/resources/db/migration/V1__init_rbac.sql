CREATE TABLE IF NOT EXISTS users (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户 ID',
    username    VARCHAR(50)  NOT NULL UNIQUE      COMMENT '用户名字',
    password    VARCHAR(100) NOT NULL             COMMENT '用户密码',
    email       VARCHAR(100) NOT NULL UNIQUE      COMMENT '用户邮箱',
    create_time BIGINT       NOT NULL             COMMENT '创建时间（时间戳）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS roles (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色 ID',
    name        VARCHAR(50)  NOT NULL UNIQUE      COMMENT '角色名称',
    description VARCHAR(200)                      COMMENT '角色描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS permissions (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限 ID',
    code        VARCHAR(100) NOT NULL UNIQUE      COMMENT '权限码',
    description VARCHAR(200)                      COMMENT '权限描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL COMMENT '用户 ID',
    role_id BIGINT NOT NULL COMMENT '角色 ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id       BIGINT NOT NULL COMMENT '角色 ID',
    permission_id BIGINT NOT NULL COMMENT '权限 ID',
    PRIMARY KEY (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

CREATE TABLE IF NOT EXISTS menus (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '菜单 ID',
    name          VARCHAR(50) NOT NULL              COMMENT '菜单名称',
    path          VARCHAR(100)                      COMMENT '路由路径',
    parent_id     BIGINT                            COMMENT '父菜单 ID',
    sort          INT                               COMMENT '排序权重',
    required_role VARCHAR(50)                       COMMENT '可见角色限制'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单树表';

CREATE TABLE IF NOT EXISTS operation_logs (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    operator    VARCHAR(100)                      COMMENT '操作人用户名',
    action      VARCHAR(100)                      COMMENT '操作动作描述',
    target      VARCHAR(100)                      COMMENT '操作目标类名',
    target_id   VARCHAR(100)                      COMMENT '操作目标的业务ID',
    result      VARCHAR(100)                      COMMENT '操作结果',
    create_time BIGINT                            COMMENT '操作时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
