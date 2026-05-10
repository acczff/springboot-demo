package com.zff.springboot_demo.operationlog;

import java.lang.annotation.*;

@Target(ElementType.METHOD)       // 只能加在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时可读取
@Documented
public @interface LogOperation {
    String value() default "";    // 操作描述，如"新增角色"
}
