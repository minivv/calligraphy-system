package com.calligraphy.config;

import com.calligraphy.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            sendError(response, "未登录，请先登录");
            return false;
        }
        try {
            Claims claims = JwtUtils.parseToken(token);
            request.setAttribute("userId", Long.valueOf(claims.get("id").toString()));
            request.setAttribute("username", claims.get("username").toString());
            request.setAttribute("role", claims.get("role").toString());
            return true;
        } catch (Exception e) {
            sendError(response, "token无效，请重新登录");
            return false;
        }
    }

    private void sendError(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("msg", msg);
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
