package com.zff.springboot_demo.config;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.exception.BusinessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.MDC;

import java.io.IOException;

/**
 * 全局异常处理器，将常见异常转换成统一响应格式。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验失败异常。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(MethodArgumentNotValidException ex) {
        // 拿到第一条校验失败的错误信息
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");
        return errorWithTraceId(400, message);
    }

    /**
     * 处理业务参数不合法异常。
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return errorWithTraceId(400, ex.getMessage() + "：处理业务参数不合法异常。");
    }

    /**
     * 处理文件读写异常。
     */
    @ExceptionHandler(IOException.class)
    public Result<String> handleIOException(IOException ex) {
        return errorWithTraceId(500, ex.getMessage() + "：处理文件读写异常。");
    }

    /**
     * 处理业务异常（账号不存在、密码错误、权限不足等），保留自定义状态码。
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException ex) {
        return errorWithTraceId(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理未单独声明的运行时异常。
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException ex) {
        return errorWithTraceId(500, ex.getMessage() + "： 处理未单独声明的运行时异常。");
    }

    /**
     * 处理请求体为空或 JSON 格式错误。
     */
    @ExceptionHandler
    public Result<String> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return errorWithTraceId(400, "请求体不能为空或格式错误");
    }

    private Result<String> errorWithTraceId(Integer code, String message) {
        return Result.error(code, message, MDC.get("traceId"));
    }

}
