package com.zff.springboot_demo.dto.login;

/**
 * 登录请求 DTO
 * 用于接收前端传来的登录数据
 */
public class LoginRequest {


    /**
     * 账号（用户名或邮箱）
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    // 默认构造函数（必须有）
    public LoginRequest() {};

    // 全参构造函数
    public LoginRequest(String account, String password) {
        this.account = account;
        this.password = password;
    }
    // Getter 和 Setter
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
