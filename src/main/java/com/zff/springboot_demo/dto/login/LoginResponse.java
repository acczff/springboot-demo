package com.zff.springboot_demo.dto.login;

/**
 * 登录响应 DTO
 * 用于返回登录成功后的数据
 */
public class LoginResponse {
    /**
     * 登录令牌（用于后续请求的身份验证）
     */
    private String token;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    //默认构造
    public LoginResponse() {}

    // 全参构造函数
    public LoginResponse(String token, Long userId, String username, String email) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    // Getter 和 Setter
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
    public Long getUserId() {return userId;}
    public void setUserId(Long id) {this.userId = id;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

}

