package com.zff.springboot_demo.user.entity;

import jakarta.persistence.*;

/**
 * 用户实体类
 * 对应数据库中的 users 表
 * - @Entity ：告诉 JPA 这是一个实体类，对应数据库表
 * - @Table(name = "users") ：指定对应的表名是 users
 * - @Id ：标记主键字段
 * - @GeneratedValue(strategy = GenerationType.IDENTITY) ：主键自增
 * - @Column(name = "username") ：指定对应的列名
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 用户ID

    @Column(name = "username", nullable = false, length = 50)
    private String username;            // 用户名字

    @Column(name = "email", nullable = false,length = 100)
    private String email;               // 用户邮箱

    @Column(name = "create_time", nullable = false )
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
