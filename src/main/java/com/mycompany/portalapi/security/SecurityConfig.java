package com.mycompany.portalapi.security;


import com.mycompany.portalapi.constants.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        return httpSecurity.authorizeHttpRequests(auth -> {
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
                    auth.requestMatchers(HttpMethod.GET, "api/v1/files/student-profiles/*").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "api/v1/auth/authenticate").permitAll();

                    auth.requestMatchers(HttpMethod.GET, "api/v1/students/pagination/*/*").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "api/v1/files/student-profiles/*").hasAnyAuthority(RoleName.ADMIN.getValue(), RoleName.STUDENT.getValue());
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/files/student-profiles/*").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/students").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/students/*").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/students/*").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/auth/lock/**").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.PUT, "api/v1/auth/unlock/**").hasAuthority(RoleName.ADMIN.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/posts").hasAnyAuthority(RoleName.ADMIN.getValue(),RoleName.TEACHER.getValue());
                    auth.requestMatchers(HttpMethod.POST, "api/v1/files/post-files/*").hasAnyAuthority(RoleName.ADMIN.getValue(),RoleName.TEACHER.getValue());
               //     auth.requestMatchers(HttpMethod.GET, "api/v1/files/post-files/**").hasAnyAuthority(RoleName.ADMIN.getValue(),RoleName.TEACHER.getValue(),RoleName.STUDENT.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/files/post-files/*/*").hasAnyAuthority(RoleName.ADMIN.getValue(),RoleName.TEACHER.getValue(),RoleName.STUDENT.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/posts/pagination/*/*").hasAnyAuthority(RoleName.STUDENT.getValue(),RoleName.ADMIN.getValue(), RoleName.TEACHER.getValue());
                    auth.requestMatchers(HttpMethod.GET, "api/v1/posts/**").permitAll();
                }).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}