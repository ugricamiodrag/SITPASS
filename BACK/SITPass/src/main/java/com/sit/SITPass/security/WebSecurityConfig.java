package com.sit.SITPass.security;

import com.sit.SITPass.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    private TokenUtils tokenUtils;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Specify your allowed origins here
        configuration.setAllowedMethods(List.of("*")); // Allow all methods
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> corsConfigurationSource())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(restAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/users/check-email", "/api/users/logOut", "/api/facilities", "/api/images/**", "/api/users/email", "/api/comments/**", "/api/index/**","/api/comments/user/**", "/api/file/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/login", "/api/users/register", "/api/facilities/search", "/api/comments", "/api/search/simple", "/api/search/advance", "/api/index/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/file/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/manages/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.OPTIONS, "/api/file/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/change-password").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/requests", "/api/reviews/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new AuthenticationTokenFilter(userDetailsService(), tokenUtils), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .ignoring()
                .requestMatchers(HttpMethod.POST, "/api/users/login", "/api/comments", "/api/users/email", "/api/facilities/unvisited", "/api/analytics/reviews",
                        "api/analytics/custom", "/api/analytics/users", "/api/search/simple", "/api/search/advance", "/api/index/**")
                .requestMatchers(HttpMethod.GET, "/api/users/check-email", "/api/users/logOut", "/api/facilities", "/api/reviews/**",
                        "/api/manages/**", "/api/comments/**", "/api/comments/user/**", "/api/facilities/popular", "/api/facilities/visited", "/api/facilities/managed", "/api/index/**", "/api/file/**")
                .requestMatchers(HttpMethod.PUT, "/api/reviews/**")
                .requestMatchers(HttpMethod.DELETE, "/api/file/**")
                .requestMatchers(HttpMethod.OPTIONS, "/api/file/**")
                .requestMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js");
    }
}
