package com.example.artforyourheart.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.artforyourheart.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Dependency injection
    @Autowired
    UserRepository userRepository;

    // Necessary for the frontend to receive and manipulate user data
    @Autowired
    private ObjectMapper objectMapper;

    // Password encoder to encrypt user passwords
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Defines security rules for different HTTP requests and paths by chaining
    // methods
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        // Define access control for specified paths
                        .requestMatchers("/", "/home", "/public/**", "/login", "/main", "/users", "/users/login",
                                "/likes", "/likes/matches", "/users/**", "/signup", "/perform_login", "/messages/**")
                        .permitAll()
                        .anyRequest().authenticated() // requires authentication for any request not matched above
                )
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/perform_login")
                        .successHandler(successHandler())
                        // .defaultSuccessUrl("/main", true) // Redirects to "/main" after successful
                        // login
                        .permitAll())
                .logout(logout -> logout.permitAll() // Allows logging out for everyone
                );

        return http.build();
    }

    // CORS configuration to allow backend (8080) to receive requests from the
    // frontend (3000)
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

    // Handles successful authentication
    // Prepares response to send user data (minus password) in JSON format after
    // authenticating successfully
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String username = authentication.getName();
            com.example.artforyourheart.model.User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setPassword(null); // Setting password to null to avoid giving the user/frontend access to the
                                        // password and to prevent being saved in localStorage
                response.setContentType("application/json;charset=UTF-8"); // Setting response to JSON
                response.getWriter().write(objectMapper.writeValueAsString(user));
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                // Handle the case where the user is not found (should not happen if
                // authentication was successful)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        };
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // Will be created before any other beans
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Responsible for configuring
                                                                                        // CORS
        CorsConfiguration config = new CorsConfiguration(); // Configuration settings for CORS
        config.setAllowCredentials(true); // Allows session cookies to be sent back and forth
        config.addAllowedOrigin("http://localhost:3000"); // Allows requests from localhost:3000 to be allowed
        config.addAllowedHeader("*"); // Allow all headers
        config.addAllowedMethod("*"); // Allow all methods
        source.registerCorsConfiguration("/**", config); // Apply CORS configuration to all paths
        return new CorsFilter(source); // Applies CORS settings to all *incoming* requests
    }

    // Loads user details during authentication
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            com.example.artforyourheart.model.User user = userRepository.findByUsername(username);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    getAuthorities(user));
        };
    }

    // Handles user roles and authorities, giving users permission to access certain
    // features
    private Collection<? extends GrantedAuthority> getAuthorities(com.example.artforyourheart.model.User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    // Manages authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
