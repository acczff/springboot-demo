package com.zff.springboot_demo.user.entity;

/**
 * 用户实体类
 * 对应数据库中的 users 表
 */
public class User {
    private Long id;                    // 用户ID
    private String username;            // 用户名字
    private String email;               // 用户邮箱
    private Long createTime;            //  创建时间

    public User() {
    }

    public User(Long id, String username, String email, Long createTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createTime = createTime;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
