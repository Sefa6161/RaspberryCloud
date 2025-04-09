package com.Projekt.RaspberryCloud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // http.csrf(csrf -> csrf.disable())
        // .authorizeHttpRequests(auth ->
        // auth.requestMatchers("/api/**").permitAll().anyRequest().authenticated())
        // .userDetailsService(userDetailsService);

        // Freischalten aller Endpunkte für Entwicklungsphase
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .userDetailsService(userDetailsService);

        return http.build();
    }

}
