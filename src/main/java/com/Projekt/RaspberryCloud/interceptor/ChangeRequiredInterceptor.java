package com.Projekt.RaspberryCloud.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.Projekt.RaspberryCloud.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ChangeRequiredInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String path = request.getServletPath();
        if (path.equals("/change_username") ||
                path.equals("/change_password") ||
                path.startsWith("/style.css") ||
                path.equals("/login") ||
                path.equals("/signup") ||
                path.equals("/logout")) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
                && auth.isAuthenticated()
                && !(auth.getPrincipal() instanceof String)) {

            User user = (User) auth.getPrincipal();

            if (user.isPasswordChangeRequired()) {
                response.sendRedirect(request.getContextPath() + "/change_password");
                return false;
            }

            if (user.isUsernameChangeRequired()) {
                response.sendRedirect(request.getContextPath() + "/change_username");
                return false;
            }
        }
        return true;
    }
}
