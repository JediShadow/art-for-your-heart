package com.example.artforyourheart.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF and CORS are configured outside of Spring Security
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(authz -> authz
                        // apply the lambda-based configuration
                        .requestMatchers("/", "/home", "/public/**", "/login","/users/main", "/main","/users**", "/users", "/users/login", "/likes", "/likes/matches").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
                .logout(logout -> logout.permitAll());
        return http.build();
    }

    // CORS configuration to allow backend (8080) to receive requests from the frontend (3000)
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // necessary if we implement cookies, authorization headers with HTTPS
        config.addAllowedOrigin("http://localhost:3000"); // replace with frontend url if necessary
        config.addAllowedHeader("*"); // allow all headers
        config.addAllowedMethod("*"); // allow all methods
        source.registerCorsConfiguration("/**", config); // apply these settings to all routes
        return source;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // This allows session cookies to be sent back and forth
        config.addAllowedOrigin("http://localhost:3000"); // This should be the exact URL of the app
        config.addAllowedHeader("*"); // Allow all headers
        config.addAllowedMethod("*"); // Allow all methods
        source.registerCorsConfiguration("/**", config); // Apply CORS configuration to all paths
        return new CorsFilter(source);
    }
}
