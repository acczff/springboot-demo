package com.zff.springboot_demo;

/**
 * 统一返回结果类
 * 用于包装所有接口的响应数据
 * @param <T> 数据类型（泛型）
 */

public class Result<T> {
    /**
     * 状态码
     * 200: 成功
     * 400: 请求参数错误
     * 404: 资源不存在
     * 500: 服务器内部错误
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 实际数据（泛型，可以是任何类型）
     */
    private T data;
    // 构造方法（无参）
    public Result() {
    }
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    // ========== 静态工厂方法 ==========
    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<T>(200, "success", null);
    }
    /**
     * 成功响应（有数据）
     */
    public static <T> Result<T> success(String message) {
        return new Result<T>(200, message,null);
    }
    /**
     * 成功响应（自定义消息 + 数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(200, message, data);
    }
    /**
     * 错误响应
     */
    public static <T> Result<T> error(String message) {
        return new Result<T>(500, message, null);
    }

    /**
     * 错误响应
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<T>(code, message, null);
    }

    /**
     * 错误响应（指定状态码）
     */
    public static <T> Result<T> error(Integer code, String message, T data) {
        return new Result<T>(code, message, data);
    }
    // ========== Getter 和 Setter 方法 ==========
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
