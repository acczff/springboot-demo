package com.zff.springboot_demo.user.entity;

import com.zff.springboot_demo.role.entity.Role;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

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

    @NotBlank(message = "用户名不能为空")
    @Column(name = "username", nullable = false, length = 50)
    private String username;            // 用户名字

    @NotBlank(message = "密码不能为空")
    @JsonIgnore
    @Column(name = "password", nullable = false, length = 100)
    private String password;            // 用户密码

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Column(name = "email", nullable = false,length = 100)
    private String email;               // 用户邮箱

    @Column(name = "create_time", nullable = false )
    private Long createTime;            //  创建时间

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
