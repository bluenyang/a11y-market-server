package com.multicampus.gamesungcoding.a11ymarketserver.common.config;

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
@RequiredArgsConstructor
public class SecurityConfig {

    private final List<String> allowedOriginPatterns;
    private final JwtAuthenticationFilter authenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authCfg) throws Exception {
        return authCfg.getAuthenticationManager();
    }

    // CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cfg = new CorsConfiguration();

        // application.yaml 변경됨
        cfg.setAllowedOrigins(allowedOriginPatterns);
        cfg.addAllowedHeader("*");
        cfg.addAllowedMethod("*");
        cfg.setAllowCredentials(true);

        var src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/health",
                                "/api/*/auth/login",
                                "/api/*/auth/join",
                                "/api/*/auth/refresh",
                                "/api/*/auth/oauth2/code/kakao",
                                "/api/*/products",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/connection/test")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
