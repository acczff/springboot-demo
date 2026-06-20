package com.zff.springboot_demo.operationlog;

import java.lang.annotation.*;

/**
 * 操作日志注解，标记需要被 AOP 自动记录的业务方法。
 */
@Target(ElementType.METHOD)       // 只能加在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时可读取
@Documented
public @interface LogOperation {
    String value() default "";    // 操作描述，如"新增角色"
}
