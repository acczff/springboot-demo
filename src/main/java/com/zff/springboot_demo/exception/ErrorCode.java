package com.zff.springboot_demo.exception;

/**
 * 业务错误码枚举：集中管理全项目的状态码与默认消息。
 * 使用方：throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
 */
public enum ErrorCode {

    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
