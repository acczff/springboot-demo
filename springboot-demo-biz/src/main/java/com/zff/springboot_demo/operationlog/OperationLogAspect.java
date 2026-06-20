package com.zff.springboot_demo.operationlog;

import com.zff.springboot_demo.operationlog.entity.OperationLog;
import com.zff.springboot_demo.operationlog.service.OperationLogService;
import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 操作日志切面，拦截带 @LogOperation 的方法并写入日志。
 */
@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogService operationLogService;
    private final UserService userService;

    public OperationLogAspect(OperationLogService operationLogService, UserService userService) {
        this.operationLogService = operationLogService;
        this.userService = userService;
    }

    /**
     * 拦截所有带 @LogOperation 注解的方法，方法正常返回后记录操作人和动作。
     */
    @AfterReturning("@annotation(logOperation)")
    public void recordLog(JoinPoint joinPoint, LogOperation logOperation) {
        // 1. 从当前请求里取 userId
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        Long userId = (Long) request.getAttribute("currentUserId");

        // 2. 查用户名
        String operator = "未知用户";
        if (userId != null) {
            try {
                operator = userService.findById(userId).getUsername();
            } catch (Exception e) {
                // 远程调用失败兜底
            }
        }

        // 3. 构造日志
        OperationLog log = new OperationLog();
        log.setOperator(operator);
        log.setAction(logOperation.value()); // 读注解里写的描述
        log.setTarget(joinPoint.getSignature().getDeclaringType().getSimpleName());
        log.setResult("success");
        log.setCreateTime(System.currentTimeMillis());

        operationLogService.save(log);
    }
}
