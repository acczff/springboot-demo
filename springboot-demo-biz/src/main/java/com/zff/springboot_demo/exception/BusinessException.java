package com.zff.springboot_demo.exception;

/**
 * 业务异常：携带自定义状态码，由 GlobalExceptionHandler 统一处理并注入 traceId。
 * 推荐用 ErrorCode 枚举抛出，避免 magic number：
 *   throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
 *   throw new BusinessException(ErrorCode.UNAUTHORIZED);  // 使用枚举默认消息
 */
public class BusinessException extends RuntimeException {

    private final int code;

    /** 原始构造：直接指定 code + 自定义消息 */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /** 推荐：从枚举取 code，自定义消息覆盖默认消息 */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    /** 推荐：从枚举取 code 和默认消息 */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
