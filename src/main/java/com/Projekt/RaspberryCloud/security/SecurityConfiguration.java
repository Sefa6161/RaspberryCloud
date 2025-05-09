package com.Projekt.RaspberryCloud.security;

import com.Projekt.RaspberryCloud.handler.CustomAuthSuccessHandler;
import com.Projekt.RaspberryCloud.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                                "/web/user/**")
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
                                                .failureUrl("/login?error=true")
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/api/**"));

                return http.build();
        }

        // @Bean
        // @Order(3)
        // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
        // Exception {

        // // Alle Endpunkte absichern für authentifizierte User
        // http.csrf(csrf -> csrf.disable())
        // .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        // .userDetailsService(userDetailsService);

        // return http.build();
        // }

        // @Bean
        // @Order(4)
        // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
        // Exception {

        // // Freischalten aller Endpunkte für Entwicklungsphase
        // http.csrf(csrf -> csrf.disable())
        // .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        // .userDetailsService(userDetailsService);

        // return http.build();
        // }

}
