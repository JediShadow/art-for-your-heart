package com.example.artforyourheart.security;

import com.example.artforyourheart.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CorsFilter;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                // Define access control for specified paths
                                .requestMatchers("/", "/home", "/public/**", "/login", "/main", "/users", "/users/login", "/likes", "/likes/matches", "/users/**", "/signup", "/perform_login").permitAll()
                                .anyRequest().authenticated() //requires authentication for any request not matched above
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginProcessingUrl("/perform_login")
                                .successHandler(successHandler())
//                                .defaultSuccessUrl("/main", true) // Redirects to "/main" after successful login
                                .permitAll()
                )
                .logout(logout ->
                        logout.permitAll() // Allows logging out for everyone
                );

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
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            response.setHeader("Location", "http://localhost:3000/main");
            response.setStatus(HttpServletResponse.SC_OK);
        };
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // session cookies sent back and forth
        config.addAllowedOrigin("http://localhost:3000"); // home URL of app
        config.addAllowedHeader("*"); // allow all headers
        config.addAllowedMethod("*"); // allow all methods
        source.registerCorsConfiguration("/**", config); // apply CORS configuration to all paths
        return new CorsFilter(source);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            com.example.artforyourheart.model.User user = userRepository.findByUsername(username);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
        };
    }

    private Collection<? extends GrantedAuthority> getAuthorities(com.example.artforyourheart.model.User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
