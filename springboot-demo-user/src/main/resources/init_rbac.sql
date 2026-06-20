CREATE TABLE IF NOT EXISTS roles (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS permissions (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    code        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE menus (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    path VARCHAR(100),
    parent_id BIGINT,
    sort INT
);