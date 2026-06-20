package com.zff.springboot_demo.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * JPA 审计配置类，开启自动审计并提供当前操作人 ID。
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Component
    public static class RequestAuditorAware implements AuditorAware<Long> {
        @Override
        public Optional<Long> getCurrentAuditor() {
            // 从 RequestContext 中获取当前 HTTP 请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 获取 AuthInterceptor 存入的当前用户 ID
                Long userId = (Long) request.getAttribute("currentUserId");
                if (userId != null) {
                    return Optional.of(userId);
                }
            }
            // 如果不在 Web 请求中（比如后台定时任务），返回 empty
            return Optional.empty();
        }
    }
}
