package com.mycompany.portalapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userService;
    @Override
    protected void doFilterInternal(
            @NonNull  HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

       String token = jwtUtils.getToken(request);

       if(token == null || !token.startsWith("Bearer ")){
           log.info("in null or invalid token condition {}", token);
           filterChain.doFilter(request, response);
           return;
       }
       token = token.substring(7);
       String userEmail = jwtUtils.getUserEmailByJWT(token);
       log.info("user email {}",userEmail);
       if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
           UserDetails userDetails = userService.loadUserByUsername(userEmail);
           log.info("user details {}", userDetails.getAuthorities());
           log.info("validate token :{}",jwtUtils.validateToken(token));
           Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
           userDetails.getAuthorities().forEach(role -> {
               authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
           });
           if(jwtUtils.validateToken(token)){
               UsernamePasswordAuthenticationToken authenticationToken =
                       new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
               log.info("user if token is valid authorities {}", userDetails.getAuthorities());
               authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }
           filterChain.doFilter(request, response);
       }

    }
}
