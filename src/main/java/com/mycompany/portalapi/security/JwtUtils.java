package com.mycompany.portalapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mycompany.portalapi.constants.RoleName;
import com.mycompany.portalapi.models.Role;
import com.mycompany.portalapi.repositories.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
@Slf4j
public class JwtUtils {
    private final HttpServletRequest httpServletRequest;
    private final StudentRepository studentRepository;
    private static final String SECRET_KEY = "Bearer";

    public boolean testJWTOfUser(HttpServletRequest request, String userEmail) {
        String token = getToken(request);
        if (token != null) {
            try {
                JWTVerifier jwtVerifier = JWT.require(getSignInKey()).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(token);
                String email = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return userEmail.trim().equalsIgnoreCase(email);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        } else {
            return false;
        }
    }

    public String generateToken(String email, List<Role> roleNames, long day) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * day)) // 30 days
                .withIssuer(httpServletRequest.getRequestURL().toString())
                .withClaim("roles", roleNames.stream().map(role -> role.getRoleName().getValue()).toList())
                .sign(getSignInKey());
    }

    public String getUserEmailByJWT(String token) {
        log.info("token :{}",token);
        System.err.println("token :"+token);
        if (token == null) {
            throw new JWTVerificationException("token shouldn't be null or empty");
        }
        try {
            JWTVerifier jwtVerifier = JWT.require(getSignInKey()).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT.getSubject();
        } catch (Exception e) {
            throw new JWTVerificationException("Invalid token : "+e.getMessage());
        }

    }

    public String getToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        return isTokenExpired(token) && studentRepository.existsByEmail(getUserEmailByJWT(token));
    }

    public Algorithm getSignInKey() {
        return Algorithm.HMAC256(SECRET_KEY.getBytes());
    }

    public boolean isTokenExpired(String token){
        JWTVerifier jwtVerifier = JWT.require(getSignInKey()).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getExpiresAt().after(new Date());
    }

    public Claim getClaim(String token){
        JWTVerifier jwtVerifier = JWT.require(getSignInKey()).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("roles");
    }


}
