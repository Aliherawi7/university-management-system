package com.mycompany.portalapi.security;


import com.mycompany.portalapi.constants.RoleName;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
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
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.cors(Customizer.withDefaults())
                .csrf().disable()
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("api/v1/students/search**",
                                    "/v2/api-docs",
                                    "/v3/api-docs",
                                    "/v3/api-docs/**",
                                    "swagger-resources",
                                    "swagger-resources/**",
                                    "configuration/ui",
                                    "configuration/security",
                                    "configuration/security",
                                    "swagger-ui/**",
                                    "/swagger-ui.html",
                                    "webjars/**"
                            )
                            .permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/v1/files/student-profiles/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/v1/files/user-profiles/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "api/v1/auth/authenticate").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/v1/students/pagination/*/*").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "api/v1/files/student-profiles/*").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.STUDENT.getValue());
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/files/student-profiles/*").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/students").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/students/*").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/students/*").hasAnyAuthority(RoleName.ADMIN.getValue(),RoleName.STUDENT.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/students/**").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/auth/lock/**").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/auth/unlock/**").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/posts").hasAnyAuthority(RoleName.ADMIN.getValue(),RoleName.TEACHER.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/files/post-files/*").hasAnyAuthority(RoleName.ADMIN.getValue(),RoleName.TEACHER.getValue());
               //     auth.requestMatchers(HttpMethod.GET, "api/v1/files/post-files/**").hasAnyAuthority(RoleName.ADMIN.getValue(),RoleName.TEACHER.getValue(),RoleName.STUDENT.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/files/post-files/*/*").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/v1/posts/student/?**").hasAuthority(RoleName.STUDENT.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/posts/?**").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.TEACHER.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/field-of-studies/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "api/v1/field-of-studies/**").hasAnyAuthority(RoleName.ADMIN.getValue());
                    auth.anyRequest().authenticated();
                }).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/","*/"));
        configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }




}