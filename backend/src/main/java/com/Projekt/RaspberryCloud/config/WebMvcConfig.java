package com.Projekt.RaspberryCloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.Projekt.RaspberryCloud.interceptor.ChangeRequiredInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final ChangeRequiredInterceptor changeRequiredInterceptor;

    public WebMvcConfig(ChangeRequiredInterceptor passwordChangeInterceptor) {
        this.changeRequiredInterceptor = passwordChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(changeRequiredInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login",
                        "/change_password",
                        "/change_username",
                        "/signup",
                        "/logout",
                        "/style.css");
    }
}
