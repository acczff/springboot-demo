package com.zff.springboot_demo.config;

import com.zff.springboot_demo.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (!TokenUtil.isValidBearerHeader(token)) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录，请先登录\",\"data\":null}");
            return false;
        }

        // token 合法，解析 userId 存入 request，后续 Controller 直接取，不用重复解析
        Long userId = TokenUtil.extractUserId(token);
        request.setAttribute("currentUserId", userId);
        return true;
    }
}
