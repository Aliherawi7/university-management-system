package com.mycompany.umsapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mycompany.umsapi.exceptions.InvalidTokenException;
import com.mycompany.umsapi.exceptions.TokenExpiredException;
import com.mycompany.umsapi.models.hrms.Role;
import com.mycompany.umsapi.models.hrms.UserApp;
import com.mycompany.umsapi.repositories.UserRepository;
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
    private final UserRepository userRepository;
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

    public String generateToken(String email, Set<Role> roleNames, long day) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * day)) //default period time 30 days
                .withIssuer(httpServletRequest.getRequestURL().toString())
                .withClaim("roles", roleNames.stream().map(role -> role.getRoleName().getValue()).toList())
                .sign(getSignInKey());
    }

    public String getUserEmailByJWT(String token) {
        log.info("get user toke token :{}",token);
        if (token == null || token.isBlank()) {
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
        String email = getUserEmailByJWT(token);
        if(isTokenExpired(token)){
            throw new TokenExpiredException("token is expired!");
        }
        Optional<UserApp> userApp = userRepository.findByEmail(email);
        if(userApp.isEmpty()){
            throw new InvalidTokenException("Invalid token!");
        }
        return userApp.get().isEnabled();
    }

    public Algorithm getSignInKey() {
        return Algorithm.HMAC256(SECRET_KEY.getBytes());
    }

    public boolean isTokenExpired(String token){
        JWTVerifier jwtVerifier = JWT.require(getSignInKey()).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        System.out.println("----------------------- isExpired invoked --------------------------");
        log.error("expire date: {}",decodedJWT.getExpiresAt().toString());
        return decodedJWT.getExpiresAt().before(new Date());
    }


    public Claim getClaim(String token){
        JWTVerifier jwtVerifier = JWT.require(getSignInKey()).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("roles");
    }

}
