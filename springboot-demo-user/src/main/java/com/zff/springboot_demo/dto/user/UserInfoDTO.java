package com.zff.springboot_demo.dto.user;

/**
 * 用户信息 DTO
 * 用于返回用户基本信息（不包含敏感信息）
 */
public class UserInfoDTO {

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    // 默认构造函数
    public UserInfoDTO(){};

    // 全参构造函数
    public UserInfoDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getter 和 Setter
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
}
