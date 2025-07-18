package com.Projekt.RaspberryCloud.security;

import com.Projekt.RaspberryCloud.handler.CustomAuthSuccessHandler;
import com.Projekt.RaspberryCloud.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        private final UserDetailsService userDetailsService;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final CustomAuthSuccessHandler customAuthSuccessHandler;

        public SecurityConfiguration(UserDetailsService userDetailsService,
                        JwtAuthenticationFilter jwtAuthenticationFilter, JwtService jwtService,
                        CustomAuthSuccessHandler customAuthSuccessHandler) {
                this.userDetailsService = userDetailsService;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.customAuthSuccessHandler = customAuthSuccessHandler;
        }

        @Bean
        @Order(1)
        public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/api/mobile/**").csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(
                                                auth -> auth.requestMatchers("/api/mobile/auth/**").permitAll()
                                                                .anyRequest().authenticated())
                                .userDetailsService(userDetailsService)
                                .addFilterBefore(jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/web/**", "/login", "/logout",
                                "/dashboard/**", "api/web/**",
                                "/signup", "style.css", "/change_password", "/change_username", "/files/**",
                                "/web/user/**", "api/*")
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/web/public/**", "/logout",
                                                                "/api/web/signup", "/style.css", "/signup",
                                                                "/webjars/**", "/js/**", "/css/**", "/change_password")
                                                .permitAll()
                                                .requestMatchers("/login").anonymous()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .successHandler(customAuthSuccessHandler)
                                                .failureHandler((request, response, exception) -> {
                                                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                                        response.getWriter().write("Login failed");
                                                })
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .cors(withDefaults())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/api/**", "/login", "/logout"));

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:5173"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(List.of("*"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);

                return source;
        }

}
