package com.mycompany.umsapi.security;


import com.mycompany.umsapi.constants.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter authenticationFilter;
    @Value("${allowed.origin")
    private String crossOrigin;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                                    "/v2/api-docs",
                                    "/v3/api-docs",
                                    "/v3/api-docs/**",
                                    "swagger-resources",
                                    "swagger-resources/**",
                                    "configuration/ui",
                                    "configuration/security",
                                    "swagger-ui/**",
                                    "/swagger-ui.html",
                                    "webjars/**")
                            .permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/v1/files/student-profiles/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/v1/files/user-profiles/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/v1/students/pagination/*/*").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "api/v1/files/student-profiles/*").hasAnyAuthority(RoleName.SUPER_ADMIN.getValue(),RoleName.ADMIN.getValue(), RoleName.STUDENT.getValue());
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/files/student-profiles/*").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.SUPER_ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/students").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.SUPER_ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/students/**").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.SUPER_ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll();
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/users/**").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.SUPER_ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/posts").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.SUPER_ADMIN.getValue(), RoleName.TEACHER.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/files/post-files/*").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.SUPER_ADMIN.getValue(), RoleName.TEACHER.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/files/post-files/*/*").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/v1/posts/student/?**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "api/v1/posts/**").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.TEACHER.getValue());
                  //  auth.requestMatchers(HttpMethod.GET, "api/v1/faculties/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/v1/subjects/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "api/v1/faculties/**").hasAnyAuthority(RoleName.SUPER_ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.DELETE, "api/v1/faculties/**").hasAnyAuthority(RoleName.SUPER_ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/subjects/**").hasAnyAuthority(RoleName.SUPER_ADMIN.getValue());
                    auth.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/", "*/"));
        configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}