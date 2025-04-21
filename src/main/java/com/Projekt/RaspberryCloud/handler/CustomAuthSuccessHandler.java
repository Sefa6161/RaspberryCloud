package com.Projekt.RaspberryCloud.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.Projekt.RaspberryCloud.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        User user = (User) authentication.getPrincipal();
        if (user.isPasswordChangeRequired()) {
            getRedirectStrategy().sendRedirect(request, response, "/change_password");
        } else if (user.isUsernameChangeRequired()) {
            getRedirectStrategy().sendRedirect(request, response, "/change_username");
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
