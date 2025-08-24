package com.springAWS.project.aws.spring.intrgration.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration

public class SecurityConfig {


    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // disable CSRF for simplicity (not for production)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // no auth required
                        .anyRequest().authenticated() // everything else requires login
                ).httpBasic(httpSecurityHttpBasicConfigurer -> {})
                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
